package krjakbrjak.bazel.impl;

import krjakbrjak.bazel.*;

import java.io.IOException;
import java.util.Collection;

public class ExecutableImpl implements Executable {
    private final Collection<String> command;

    public ExecutableImpl(Collection<String> command) {
        this.command = command;
    }

    @Override
    public Handle<Result> run(String workspacePath, Collection<String> options, CommandLogger logger) throws IOException {
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
