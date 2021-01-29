package krjakbrjak.bazel.plugin.project.execution;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.externalSystem.model.ExternalSystemException;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskId;
import com.intellij.openapi.externalSystem.model.task.ExternalSystemTaskNotificationListener;
import com.intellij.openapi.externalSystem.task.ExternalSystemTaskManager;
import krjakbrjak.bazel.ExecutableContext;
import krjakbrjak.bazel.Handle;
import krjakbrjak.bazel.Library;
import krjakbrjak.bazel.Result;
import krjakbrjak.bazel.plugin.settings.BazelExecutionSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;

public class BazelTaskManager implements ExternalSystemTaskManager<BazelExecutionSettings> {
    private final Map<ExternalSystemTaskId, Handle<Result>> handles = new ConcurrentHashMap<>();

    @Override
    public boolean cancelTask(@NotNull ExternalSystemTaskId id, @NotNull ExternalSystemTaskNotificationListener listener) throws ExternalSystemException {
        var handle = handles.remove(id);
        if (handle != null) {
            handle.cancel(true);
        }
        listener.onCancel(id);
        return true;
    }

    @Override
    public void executeTasks(
            @NotNull ExternalSystemTaskId id,
            @NotNull List<String> taskNames,
            @NotNull String projectPath,
            @Nullable BazelExecutionSettings settings,
            @Nullable String jvmParametersSetup,
            @NotNull ExternalSystemTaskNotificationListener listener
    ) throws ExternalSystemException {
        listener.onStart(id, projectPath);
        Optional<String> opt = taskNames.stream().findFirst();
        String task = opt.orElse("build");
        ExecutableContext exeCtx = ServiceManager.getService(Library.class).getContext()
                .getExecutableBuilder()
                .withCommand(settings.getExecutable())
                .build();
        try {
            List<String> args = new ArrayList<>(List.of(task, settings.getTarget()));
            if (settings.getArguments().size() > 0) {
                args.add("--");
                args.addAll(settings.getArguments());
            }
            Handle<Result> handle = exeCtx.getExecutable()
                    .run(projectPath,
                            args,
                            (line, isError) -> listener.onTaskOutput(id, String.format("> %s\n", line), true));
            handles.put(id, handle);
            handle.onExit().join();
        } catch (CompletionException | IOException e) {
            throw new ExternalSystemException(e.getCause().getLocalizedMessage());
        } finally {
            var handle = handles.remove(id);
            if (handle != null) {
                handle.cancel(true);
            }
        }
    }
}
