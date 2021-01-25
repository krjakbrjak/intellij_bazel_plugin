package krjakbrjak.bazel.plugin.services;

import krjakbrjak.bazel.BazelCommands;
import krjakbrjak.bazel.CommandLogger;
import krjakbrjak.bazel.ExecutableContext;
import krjakbrjak.bazel.Handle;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;

public class BazelCommandsTest implements BazelCommands {
    private final BazelCommands mockBazel = mock(BazelCommands.class);

    public BazelCommands getMockBazel() {
        return mockBazel;
    }

    @Override
    public Handle<List<String>> queryAllPackages(ExecutableContext ctx, String workspacePath, CommandLogger logger) throws IOException {
        return mockBazel.queryAllPackages(ctx, workspacePath, logger);
    }

    @Override
    public Handle<List<String>> queryAllDependencies(ExecutableContext ctx, String workspacePath, String target) throws IOException {
        return mockBazel.queryAllDependencies(ctx, workspacePath, target);
    }

    @Override
    public Handle<Optional<String>> queryLocalJdk(ExecutableContext ctx, String workspacePath) throws IOException {
        return mockBazel.queryLocalJdk(ctx, workspacePath);
    }

    @Override
    public Handle<List<String>> queryAllTargets(ExecutableContext ctx, String workspacePath, String pkg, CommandLogger logger) throws IOException {
        return mockBazel.queryAllTargets(ctx, workspacePath, pkg, logger);
    }
}
