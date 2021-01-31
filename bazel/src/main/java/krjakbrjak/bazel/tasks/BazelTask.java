package krjakbrjak.bazel.tasks;

import java.util.List;

/**
 * An interface for a bazel task.
 */
public interface BazelTask {
    /**
     * Returns the name of the task, i.e. run/clean/etc.
     *
     * @return Name of the task.
     */
    String getName();

    /**
     * Returns task's description.
     *
     * @return Task's description.
     */
    String getDescription();

    /**
     * Returns a name of the task that can be displayed in teh UI.
     *
     * @return Task's display name.
     */
    String getDisplayName();

    /**
     * Returns a list of extra options for the task.
     *
     * @return {@code List<String>} object.
     */
    default List<String> getOptions() {
        return List.of();
    }
}
