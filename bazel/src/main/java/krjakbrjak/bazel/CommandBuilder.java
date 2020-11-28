package krjakbrjak.bazel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The helper class to build the command.
 *
 * <p>It is a tiny class that allows to set the command and its arguments separately.
 *
 * @param <E> A subclass of a {@link krjakbrjak.bazel.CommandBuilder}. Recursive parameters make it
 *            easier to apply inheritance to a 'Fluent Builder Interface'.
 */
public class CommandBuilder<E extends CommandBuilder<E>> {
    private final List<String> args = new ArrayList<>();
    private Collection<String> command;
    private String workingDir;

    @SuppressWarnings("unchecked")
    public E command(Collection<String> command) {
        this.command = command;
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    public E workingDir(String workingDir) {
        this.workingDir = workingDir;
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    public E args(String arg) {
        this.args.add(arg);
        return (E) this;
    }

    @SuppressWarnings("unchecked")
    public E args(Collection<String> args) {
        this.args.addAll(args);
        return (E) this;
    }

    public List<String> getCommand() {
        return Stream.concat(command.stream(), args.stream()).collect(Collectors.toList());
    }

    public String getWorkingDir() {
        return workingDir;
    }

    /**
     * Starts the command asynchronously.
     *
     * @param logger The logger to catch program's output. See {@link krjakbrjak.bazel.CommandLogger}
     * @return The new {@link krjakbrjak.bazel.Result} object.
     */
    public CompletableFuture<Result> exec(CommandLogger logger) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return new CommandStream(
                        new ProcessBuilder()
                                .command(getCommand())
                                .directory(new File(workingDir)),
                        logger
                ).getResult();
            } catch (InterruptedException | IOException e) {
                return new Result(e.getLocalizedMessage(), List.of(), -1);
            }
        });
    }
}
