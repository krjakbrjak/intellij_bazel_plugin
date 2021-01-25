package krjakbrjak.bazel;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A basic implementation of bazel commands. Internally the process `bazel query ...`
 * is run.
 */
public class Workspace implements BazelCommands {
    @Override
    public Handle<List<String>> queryAllPackages(ExecutableContext ctx, String workspacePath, CommandLogger logger) throws IOException {
        Handle<Result> handle = ctx.getExecutable().run(workspacePath, List.of(
                "query",
                "...",
                "--output",
                "package"),
                logger);
        return new ConvertibleHandle<>(handle, result -> {
            if (result.getReturnCode() == 0) {
                return new ArrayList<>(result.getOutput());
            }
            return null;
        });
    }

    @Override
    public Handle<List<String>> queryAllDependencies(ExecutableContext ctx, String workspacePath, String target) throws IOException {
        LocationParser parser = ctx.getLocationParser();
        Handle<Result> original = ctx.getExecutable().run(workspacePath, List.of(
                "query",
                String.format("kind('%s', deps(%s))", Kind.SOURCE_FILE.toString(), target),
                "--noimplicit_deps",
                "--output",
                "location"),
                (a, b) -> {
                });
        return new ConvertibleHandle<>(original, result -> {
            if (result.getReturnCode() == 0) {
                return result.getOutput().stream()
                        .map(item -> parser.parseLocation(item, Kind.SOURCE_FILE))
                        .collect(Collectors.toList());
            }
            return List.of();
        });
    }

    @Override
    public Handle<Optional<String>> queryLocalJdk(ExecutableContext ctx, String workspacePath) throws IOException {
        LocationParser parser = ctx.getLocationParser();
        Handle<Result> original = ctx.getExecutable().run(workspacePath, List.of(
                "query",
                "kind('java_runtime', @local_jdk//:jdk)",
                "--output",
                "location"),
                (a, b) -> {
                });
        return new ConvertibleHandle<>(original, result -> {
            if (result.getReturnCode() == 0) {
                return result.getOutput().stream()
                        .map(item -> FilenameUtils.getFullPathNoEndSeparator(
                                parser.parseLocation(item, Kind.JAVA_RUNTIME_RULE)))
                        .findFirst();
            }

            return Optional.empty();
        });
    }

    @Override
    public Handle<List<String>> queryAllTargets(ExecutableContext ctx, String workspacePath, String pkg, CommandLogger logger) throws IOException {
        Handle<Result> handle = ctx.getExecutable().run(workspacePath, List.of(
                "query",
                "kind(rule, " + pkg + ":*)"),
                logger);
        return new ConvertibleHandle<>(handle, result -> {
            if (result.getReturnCode() == 0) {
                return new ArrayList<>(result.getOutput());
            }
            return null;
        });
    }
}
