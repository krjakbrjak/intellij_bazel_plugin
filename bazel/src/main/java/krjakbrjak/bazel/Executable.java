package krjakbrjak.bazel;

import java.io.IOException;
import java.util.Collection;

/**
 * An interface representing an executable.
 */
public interface Executable {
    /**
     * Runs (async) a command in the specified location.
     *
     * @param workspacePath A path where to run the command.
     * @param options       A list of extra options to the executable.
     * @param logger        {@link krjakbrjak.bazel.CommandLogger} object.
     * @return {@link krjakbrjak.bazel.Handle} that holds {@link krjakbrjak.bazel.Result}.
     * @throws IOException If an I/O error occurs.
     */
    Handle<Result> run(String workspacePath, Collection<String> options, CommandLogger logger) throws IOException;

    /**
     * Returns executable's command.
     *
     * @return {@code Collection<String>} object.
     */
    Collection<String> getCommand();
}
