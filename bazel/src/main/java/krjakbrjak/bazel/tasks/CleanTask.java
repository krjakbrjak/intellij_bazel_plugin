package krjakbrjak.bazel.tasks;

import java.util.ResourceBundle;

/**
 * Cleans current target.
 */
public class CleanTask implements BazelTask {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("Bazel");

    @Override
    public String getName() {
        return BazelTaskNames.CLEAN.getName();
    }

    @Override
    public String getDescription() {
        return resourceBundle.getString("bazel.tasks.clean.description");
    }

    @Override
    public String getDisplayName() {
        return resourceBundle.getString("bazel.tasks.clean.displayName");
    }
}
