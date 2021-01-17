package krjakbrjak.bazel.plugin.project;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.SimpleJavaParameters;
import com.intellij.openapi.externalSystem.ExternalSystemManager;
import com.intellij.openapi.externalSystem.model.ProjectSystemId;
import com.intellij.openapi.externalSystem.service.project.ExternalSystemProjectResolver;
import com.intellij.openapi.externalSystem.task.ExternalSystemTaskManager;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Function;
import krjakbrjak.bazel.plugin.project.execution.BazelTaskManager;
import krjakbrjak.bazel.plugin.settings.*;
import krjakbrjak.bazel.plugin.util.BazelConstants;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BazelManager implements ExternalSystemManager<
        BazelProjectSettings,
        BazelSettingsListener,
        BazelSettings,
        BazelLocalSettings,
        BazelExecutionSettings> {
    @Override
    public @NotNull ProjectSystemId getSystemId() {
        return BazelConstants.SYSTEM_ID;
    }

    @Override
    public @NotNull Function<Project, BazelSettings> getSettingsProvider() {
        return BazelSettings::getInstance;
    }

    @Override
    public @NotNull Function<Project, BazelLocalSettings> getLocalSettingsProvider() {
        return BazelLocalSettings::getInstance;
    }

    @Override
    public @NotNull Function<Pair<Project, String>, BazelExecutionSettings> getExecutionSettingsProvider() {
        return pair -> {
            final Project project = pair.first;
            final String projectPath = pair.second;
            return prepareExecutionSettings(project, projectPath);
        };
    }

    /**
     * Prepares execution settings (will be used to run bazel commands).
     *
     * @param project     An Intellij project.
     * @param projectPath A path to a linked bazel project.
     * @return {@link krjakbrjak.bazel.plugin.settings.BazelExecutionSettings}
     */
    public BazelExecutionSettings prepareExecutionSettings(Project project, String projectPath) {
        BazelSettings settings = BazelSettings.getInstance(project);

        BazelProjectSettings projectLevelSettings = settings.getLinkedProjectSettings(projectPath);

        List<String> targets = projectLevelSettings.getTargets();
        int currentTarget = projectLevelSettings.getCurrentTarget();
        String target = null;
        if (targets != null && targets.size() > currentTarget && currentTarget > -1) {
            target = targets.get(currentTarget);
        }
        BazelExecutionSettings result = new BazelExecutionSettings(projectLevelSettings.getExecutable(), target);
        final String rootProjectPath = projectLevelSettings != null ? projectLevelSettings.getExternalProjectPath() : projectPath;
        String ideProjectPath;
        if (project.getBasePath() == null ||
                (project.getProjectFilePath() != null && StringUtil.endsWith(project.getProjectFilePath(), ".ipr"))) {
            ideProjectPath = rootProjectPath;
        } else {
            ideProjectPath = project.getBasePath() + "/.idea/modules";
        }
        result.setIdeProjectPath(ideProjectPath);

        return result;
    }

    @Override
    public @NotNull Class<? extends ExternalSystemProjectResolver<BazelExecutionSettings>> getProjectResolverClass() {
        return BazelProjectResolver.class;
    }

    @Override
    public Class<? extends ExternalSystemTaskManager<BazelExecutionSettings>> getTaskManagerClass() {
        return BazelTaskManager.class;
    }

    @Override
    public @NotNull FileChooserDescriptor getExternalProjectDescriptor() {
        return FileChooserDescriptorFactory.createSingleFolderDescriptor();
    }

    @Override
    public void enhanceRemoteProcessing(@NotNull SimpleJavaParameters parameters) throws ExecutionException {
        // An exception is thrown here because it seems that the design of the
        // communication between the RMI server and client is not complete. Currently
        // there is no (obvious/easy/sound) way to pass the output of the server ('bazel' side)
        // to the client ('idea' side). That means that the plugin will function OK, but there will
        // be no 'bazel logs' reported. That will cause not so good user experience, thus made
        // 'remote' mode not supported atm.
        throw new UnsupportedOperationException();
    }
}
