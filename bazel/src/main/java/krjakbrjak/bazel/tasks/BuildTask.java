package krjakbrjak.bazel.tasks;

import java.util.ResourceBundle;

public class BuildTask implements BazelTask {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("Bazel");

    @Override
    public String getName() {
        return BazelTaskNames.BUILD.getName();
    }

    @Override
    public String getDescription() {
        return resourceBundle.getString("bazel.tasks.build.description");
    }

    @Override
    public String getDisplayName() {
        return resourceBundle.getString("bazel.tasks.build.displayName");
    }
}
