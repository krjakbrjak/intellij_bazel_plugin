package krjakbrjak.bazel;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * An interface for common bazel commands.
 */
public interface BazelCommands {
    /**
     * Returns all packages (directories with BUILD file) for given {@code workspacePath}.
     *
     * @param ctx           {@link krjakbrjak.bazel.ExecutableContext} object.
     * @param workspacePath A path to a directory that contains WORKSPACE file.
     * @param logger        {@link krjakbrjak.bazel.CommandLogger} object.
     * @return {@link krjakbrjak.bazel.Handle} object.
     * @throws IOException If underlying command fails to run.
     */
    Handle<List<String>> queryAllPackages(ExecutableContext ctx, String workspacePath, CommandLogger logger) throws IOException;

    /**
     * Returns all dependencies for a given {@code target}.
     *
     * @param ctx           {@link krjakbrjak.bazel.ExecutableContext} object.
     * @param workspacePath A path to a directory that contains WORKSPACE file.
     * @param target        A target.
     * @return {@link krjakbrjak.bazel.Handle} object.
     * @throws IOException If underlying command fails to run.
     */
    Handle<List<String>> queryAllDependencies(ExecutableContext ctx, String workspacePath, String target) throws IOException;

    /**
     * Returns a path to JDK provided by Bazel.
     *
     * @param ctx           {@link krjakbrjak.bazel.ExecutableContext} object.
     * @param workspacePath A path to a directory that contains WORKSPACE file.
     * @return {@link krjakbrjak.bazel.Handle} object.
     * @throws IOException If underlying command fails to run.
     */
    Handle<Optional<String>> queryLocalJdk(ExecutableContext ctx, String workspacePath) throws IOException;

    /**
     * Returns all targets defined in a package {@code pkg}.
     *
     * @param ctx           {@link krjakbrjak.bazel.ExecutableContext} object.
     * @param workspacePath A path to a directory that contains WORKSPACE file.
     * @param pkg           A package.
     * @param logger        {@link krjakbrjak.bazel.CommandLogger} object.
     * @return {@link krjakbrjak.bazel.Handle} object.
     * @throws IOException If underlying command fails to run.
     */
    Handle<List<String>> queryAllTargets(ExecutableContext ctx, String workspacePath, String pkg, CommandLogger logger) throws IOException;
}
