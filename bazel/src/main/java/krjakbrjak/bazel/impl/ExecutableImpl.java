package krjakbrjak.bazel.impl;

import krjakbrjak.bazel.CommandBuilder;
import krjakbrjak.bazel.CommandLogger;
import krjakbrjak.bazel.Executable;
import krjakbrjak.bazel.Result;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class ExecutableImpl implements Executable {
    private final Collection<String> command;

    public ExecutableImpl(Collection<String> command) {
        this.command = command;
    }

    @Override
    public CompletableFuture<Result> run(String workspacePath, Collection<String> options, CommandLogger logger) {
        return new CommandBuilder<>()
                .command(getCommand())
                .workingDir(workspacePath)
                .args(options)
                .exec(logger);
    }

    @Override
    public Collection<String> getCommand() {
        return command;
    }
}
