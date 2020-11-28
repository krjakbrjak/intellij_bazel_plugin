package krjakbrjak.bazel;

import java.util.concurrent.CompletableFuture;

/**
 * An interface for common bazel commands.
 */
public interface BazelCommands {
    /**
     * Returns all packages (directories with BUILD file) for given {@code workspacePath}.
     *
     * @param ctx           {@link krjakbrjak.bazel.ExecutableContext} object.
     * @param workspacePath A path to a directory that contains WORKSPACE file.
     * @return {@code CompletableFuture<Result>} object.
     */
    CompletableFuture<Result> queryAllPackages(ExecutableContext ctx, String workspacePath);

    /**
     * Returns all dependencies for a given {@code target}.
     *
     * @param ctx           {@link krjakbrjak.bazel.ExecutableContext} object.
     * @param workspacePath A path to a directory that contains WORKSPACE file.
     * @param target        A target.
     * @return {@code CompletableFuture<Result>} object.
     */
    CompletableFuture<Result> queryAllDependencies(ExecutableContext ctx, String workspacePath, String target);

    /**
     * Returns a path to JDK provided by Bazel.
     *
     * @param ctx           {@link krjakbrjak.bazel.ExecutableContext} object.
     * @param workspacePath A path to a directory that contains WORKSPACE file.
     * @return {@code CompletableFuture<Result>} object.
     */
    CompletableFuture<Result> queryLocalJdk(ExecutableContext ctx, String workspacePath);

    /**
     * Returns all targets defined in a package {@code pkg}.
     *
     * @param ctx           {@link krjakbrjak.bazel.ExecutableContext} object.
     * @param workspacePath A path to a directory that contains WORKSPACE file.
     * @param pkg           A package.
     * @return {@code CompletableFuture<Result>} object.
     */
    CompletableFuture<Result> queryAllTargets(ExecutableContext ctx, String workspacePath, String pkg);
}
