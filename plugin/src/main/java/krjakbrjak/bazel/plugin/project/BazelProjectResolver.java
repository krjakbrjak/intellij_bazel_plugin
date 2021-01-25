package krjakbrjak.bazel.plugin.project;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.externalSystem.model.DataNode;
import com.intellij.openapi.externalSystem.model.ExternalSystemException;
import com.intellij.openapi.externalSystem.model.ProjectKeys;
import com.intellij.openapi.externalSystem.model.project.*;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListener;
import com.intellij.openapi.externalSystem.service.project.ExternalSystemProjectResolver;
import com.intellij.openapi.module.ModuleTypeId;
import com.intellij.openapi.projectRoots.Sdk;
import krjakbrjak.bazel.*;
import krjakbrjak.bazel.plugin.project.services.JdkResolver;
import krjakbrjak.bazel.plugin.settings.BazelExecutionSettings;
import krjakbrjak.bazel.plugin.util.BazelConstants;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.CompletionException;

/**
 * Resolves bazel project info: source sets, jdk, dependencies, etc.
 */
public class BazelProjectResolver implements ExternalSystemProjectResolver<BazelExecutionSettings> {
    public boolean areExecutionSettingsValid(BazelExecutionSettings settings) {
        return settings != null && !StringUtils.isEmpty(settings.getIdeProjectPath());
    }

    @Override
    public @Nullable DataNode<ProjectData> resolveProjectInfo(
            @NotNull ExternalSystemTaskId id,
            @NotNull String projectPath,
            boolean isPreviewMode,
            @Nullable BazelExecutionSettings settings,
            @NotNull ExternalSystemTaskNotificationListener listener
    ) throws ExternalSystemException, IllegalArgumentException, IllegalStateException {
        // It is not possible to resolve the project without proper execution settings
        if (!areExecutionSettingsValid(settings)) {
            return null;
        }

        DataNode<ProjectData> ret = new DataNode<>(
                ProjectKeys.PROJECT,
                new ProjectData(
                        BazelConstants.SYSTEM_ID,
                        FilenameUtils.getName(projectPath),
                        settings.getIdeProjectPath(),
                        projectPath
                ),
                null);
        DataNode<ModuleData> module = ret.createChild(ProjectKeys.MODULE, new ModuleData(
                FilenameUtils.getName(projectPath),
                BazelConstants.SYSTEM_ID,
                ModuleTypeId.JAVA_MODULE,
                FilenameUtils.getName(projectPath),
                FilenameUtils.concat(settings.getIdeProjectPath(), ".idea/modules"),
                projectPath
        ));

        BazelCommands bazelCommands = ServiceManager.getService(BazelCommands.class);

        try {
            addModuleSdk(module, bazelCommands, settings, projectPath);
            addModuleDependencies(module, bazelCommands, settings, projectPath);
        } catch (CompletionException e) {
            try {
                throw e.getCause();
            } catch (ExternalSystemException external) {
                throw external;
            }
            // That should not be possible to enter this catch statement.
            catch (Throwable rest) {
                throw new AssertionError(rest);
            }
        }

        return ret;
    }

    @Override
    public boolean cancelTask(@NotNull ExternalSystemTaskId taskId, @NotNull ExternalSystemTaskNotificationListener listener) {
        return false;
    }

    /**
     * Retrieves a path to a local jdk and adds {@link com.intellij.openapi.externalSystem.model.project.ModuleSdkData}
     * to a {@code module}.
     *
     * @param module        Module to add jdk to.
     * @param bazelCommands Bazel commands executor.
     * @param settings      {@link krjakbrjak.bazel.plugin.settings.BazelExecutionSettings} objects.
     * @param projectPath   Project path.
     * @throws CompletionException If {@code bazelCommands} returns {@link krjakbrjak.bazel.Result} with return
     *                             code other than 0.
     */
    public void addModuleSdk(DataNode<ModuleData> module, BazelCommands bazelCommands, BazelExecutionSettings settings, String projectPath) {
        ExecutableContext exeCtx = ServiceManager.getService(Library.class).getContext()
                .getExecutableBuilder()
                .withCommand(settings.getExecutable())
                .build();
        try {
            bazelCommands.queryLocalJdk(exeCtx, projectPath)
                    .onExit()
                    .thenApplyAsync(optionalPath -> {
                        optionalPath.ifPresent(path -> {
                            Sdk sdk = ServiceManager.getService(JdkResolver.class).resolveJdk(path);
                            module.createChild(ModuleSdkData.KEY, new ModuleSdkData(sdk.getName()));
                        });
                        return null;
                    })
                    .join();
        } catch (IOException e) {
            throw new ExternalSystemException(e.getLocalizedMessage());
        }
    }

    /**
     * Adds dependencies (source, libraries) to {@code module}.
     *
     * @param bazelCommands Bazel commands executor.
     * @param settings      {@link krjakbrjak.bazel.plugin.settings.BazelExecutionSettings} objects.
     * @param projectPath   Project path.
     * @throws CompletionException If {@code bazelCommands} returns {@link krjakbrjak.bazel.Result} with return
     *                             code other than 0.
     */
    public void addModuleDependencies(DataNode<ModuleData> module, BazelCommands bazelCommands, BazelExecutionSettings settings, String projectPath) {
        ExecutableContext exeCtx = ServiceManager.getService(Library.class).getContext()
                .getExecutableBuilder()
                .withCommand(settings.getExecutable())
                .build();
        try {
            bazelCommands.queryAllDependencies(
                    exeCtx, projectPath, settings.getTarget())
                    .onExit()
                    .thenApplyAsync(deps -> {
                        LibraryData libraryData = new LibraryData(BazelConstants.SYSTEM_ID,
                                FilenameUtils.getName(projectPath));
                        deps.stream().map(Dependency::new)
                                .distinct()
                                .forEach(entry -> {
                                    if (entry.getSourceType() == SourceType.SOURCE_CODE) {
                                        String sourceSet = Dependency.getSourceSet(entry);
                                        if (sourceSet != null) {
                                            ContentRootData contentRootData = new ContentRootData(BazelConstants.SYSTEM_ID,
                                                    sourceSet);
                                            contentRootData.storePath(ExternalSystemSourceType.SOURCE, sourceSet);
                                            module.createChild(ProjectKeys.CONTENT_ROOT, contentRootData);
                                        }
                                    } else {
                                        libraryData.addPath(LibraryPathType.BINARY, entry.getPath());
                                    }
                                });
                        module.createChild(ProjectKeys.LIBRARY_DEPENDENCY,
                                new LibraryDependencyData(module.getData(), libraryData, LibraryLevel.MODULE));
                        return null;
                    })
                    .join();
        } catch (IOException e) {
            throw new ExternalSystemException(e.getLocalizedMessage());
        }
    }
}
