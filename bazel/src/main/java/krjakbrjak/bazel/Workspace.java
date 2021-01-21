package krjakbrjak.bazel;

import org.apache.commons.io.FilenameUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * A basic implementation of bazel commands. Internally the process `bazel query ...`
 * is run.
 */
public class Workspace implements BazelCommands {
    @Override
    public CompletableFuture<Result> queryAllPackages(ExecutableContext ctx, String workspacePath, CommandLogger logger) {
        return ctx.getExecutable().run(workspacePath, List.of(
                "query",
                "...",
                "--output",
                "package"),
                logger);
    }

    @Override
    public CompletableFuture<Result> queryAllDependencies(ExecutableContext ctx, String workspacePath, String target) {
        LocationParser parser = ctx.getLocationParser();
        return ctx.getExecutable().run(workspacePath, List.of(
                "query",
                String.format("kind('%s', deps(%s))", Kind.SOURCE_FILE.toString(), target),
                "--noimplicit_deps",
                "--output",
                "location"))
                .thenApply(result -> {
                            if (result.getReturnCode() == 0) {
                                return new Result(result.getError(),
                                        result.getOutput().stream()
                                                .map(item -> parser.parseLocation(item, Kind.SOURCE_FILE))
                                                .collect(Collectors.toList()),
                                        result.getReturnCode());
                            }
                            return result;
                        }
                );
    }

    @Override
    public CompletableFuture<Result> queryLocalJdk(ExecutableContext ctx, String workspacePath) {
        LocationParser parser = ctx.getLocationParser();
        return ctx.getExecutable().run(workspacePath, List.of(
                "query",
                "kind('java_runtime', @local_jdk//:jdk)",
                "--output",
                "location"))
                .thenApplyAsync(result -> {
                            if (result.getReturnCode() == 0) {
                                return new Result(result.getError(),
                                        result.getOutput().stream()
                                                .map(item -> FilenameUtils.getFullPathNoEndSeparator(
                                                        parser.parseLocation(item, Kind.JAVA_RUNTIME_RULE)))
                                                .collect(Collectors.toList()),
                                        result.getReturnCode());
                            }

                            return result;
                        }
                );
    }

    @Override
    public CompletableFuture<Result> queryAllTargets(ExecutableContext ctx, String workspacePath, String pkg) {
        return ctx.getExecutable().run(workspacePath, List.of(
                "query",
                "kind(rule, " + pkg + ":*)"));
    }
}
