package krjakbrjak.bazel.plugin.project;

import java.util.List;

/**
 * A helper interface to get settings' updates from {@link krjakbrjak.bazel.plugin.project.BazelModuleSetup} objects.
 */
public interface BazelModuleSetupListener {
    /**
     * Called when the executable got changed.
     *
     * @param executable A new eecutable.
     */
    void onBazelExecutableChanged(List<String> executable);

    /**
     * Called when a target got changed.
     *
     * @param target A new target.
     */
    void onTargetChanged(String target);

    /**
     * Called when a package got changed.
     *
     * @param pkg A new package.
     */
    void onPackageChanged(String pkg);
}
