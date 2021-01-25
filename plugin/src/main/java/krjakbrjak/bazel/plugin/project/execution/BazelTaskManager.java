package krjakbrjak.bazel.plugin.project.execution;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.externalSystem.model.ExternalSystemException;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListener;
import com.intellij.openapi.externalSystem.task.ExternalSystemTaskManager;
import krjakbrjak.bazel.ExecutableContext;
import krjakbrjak.bazel.Library;
import krjakbrjak.bazel.plugin.settings.BazelExecutionSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletionException;

public class BazelTaskManager implements ExternalSystemTaskManager<BazelExecutionSettings> {
    @Override
    public boolean cancelTask(@NotNull ExternalSystemTaskId id, @NotNull ExternalSystemTaskNotificationListener listener) throws ExternalSystemException {
        return false;
    }

    @Override
    public void executeTasks(@NotNull ExternalSystemTaskId id, @NotNull List<String> taskNames, @NotNull String projectPath, @Nullable BazelExecutionSettings settings, @Nullable String jvmParametersSetup, @NotNull ExternalSystemTaskNotificationListener listener) throws ExternalSystemException {
        listener.onStart(id, projectPath);
        ExecutableContext exeCtx = ServiceManager.getService(Library.class).getContext()
                .getExecutableBuilder()
                .withCommand(settings.getExecutable())
                .build();
        try {
            exeCtx.getExecutable()
                    .run(projectPath,
                            List.of("build", settings.getTarget()),
                            (line, isError) -> listener.onTaskOutput(id, String.format("> %s\n", line), true))
                    .onExit()
                    .join();
        } catch (CompletionException | IOException e) {
            throw new ExternalSystemException(e.getCause().getLocalizedMessage());
        }
    }
}
