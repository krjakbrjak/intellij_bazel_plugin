package krjakbrjak.bazel;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * An interface representing an executable.
 */
public interface Executable {
    default CompletableFuture<Result> run(String workspacePath, Collection<String> options) {
        return run(workspacePath, options, (line, isError) -> {
        });
    }

    /**
     * Runs (async) a command in the specified location.
     *
     * @param workspacePath A path where to run the command.
     * @param options       A list of extra options to the executable.
     * @param logger        {@link krjakbrjak.bazel.CommandLogger} object.
     * @return {@code CompletableFuture} that holds {@link krjakbrjak.bazel.Result}.
     */
    CompletableFuture<Result> run(String workspacePath, Collection<String> options, CommandLogger logger);

    /**
     * Returns executable's command.
     *
     * @return {@code Collection<String>} object.
     */
    Collection<String> getCommand();
}
