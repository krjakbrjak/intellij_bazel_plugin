package krjakbrjak.bazel.plugin.services;

import krjakbrjak.bazel.BazelCommands;
import krjakbrjak.bazel.ExecutableContext;
import krjakbrjak.bazel.Result;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;

public class BazelCommandsTest implements BazelCommands {
    private final BazelCommands mockBazel = mock(BazelCommands.class);

    public BazelCommands getMockBazel() {
        return mockBazel;
    }

    @Override
    public CompletableFuture<Result> queryAllPackages(ExecutableContext ctx, String workspacePath) {
        return mockBazel.queryAllPackages(ctx, workspacePath);
    }

    @Override
    public CompletableFuture<Result> queryAllDependencies(ExecutableContext ctx, String workspacePath, String target) {
        return mockBazel.queryAllDependencies(ctx, workspacePath, target);
    }

    @Override
    public CompletableFuture<Result> queryLocalJdk(ExecutableContext ctx, String workspacePath) {
        return mockBazel.queryLocalJdk(ctx, workspacePath);
    }

    @Override
    public CompletableFuture<Result> queryAllTargets(ExecutableContext ctx, String workspacePath, String pkg) {
        return mockBazel.queryAllTargets(ctx, workspacePath, pkg);
    }
}
