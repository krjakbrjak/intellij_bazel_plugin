package krjakbrjak.bazel.tasks;

import java.util.List;
import java.util.ResourceBundle;

/**
 * A task that will build the target (if necessary) and starts it. But it will stop
 * right before the execution and will wait for a debugger to attach to it (on port 5005).
 */
public class DebugTask implements BazelTask {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("Bazel");

    @Override
    public String getName() {
        return BazelTaskNames.RUN.getName();
    }

    @Override
    public String getDescription() {
        return resourceBundle.getString("bazel.tasks.debug.description");
    }

    @Override
    public String getDisplayName() {
        return resourceBundle.getString("bazel.tasks.debug.displayName");
    }

    @Override
    public List<String> getOptions() {
        return List.of("--debug");
    }
}
