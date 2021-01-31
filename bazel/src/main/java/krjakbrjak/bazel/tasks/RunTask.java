package krjakbrjak.bazel.tasks;

import java.util.ResourceBundle;

/**
 * A task to run bazel target.
 */
public class RunTask implements BazelTask {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("Bazel");

    @Override
    public String getName() {
        return BazelTaskNames.RUN.getName();
    }

    @Override
    public String getDescription() {
        return resourceBundle.getString("bazel.tasks.run.description");
    }

    @Override
    public String getDisplayName() {
        return resourceBundle.getString("bazel.tasks.run.displayName");
    }
}
