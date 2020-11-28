package krjakbrjak.bazel.utils;

import com.github.javaparser.ParseResult;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class JavaParser {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("Bazel");

    public static String getPackageName(File file) throws IOException {
        return getPackageName(
                FileUtils.readFileToString(file, StandardCharsets.UTF_8)
        );
    }

    public static String getPackageName(String content) throws IOException {
        com.github.javaparser.JavaParser parser = new com.github.javaparser.JavaParser();
        ParseResult<CompilationUnit> result = parser.parse(content);
        if (!result.isSuccessful()) {
            throw new IOException(
                    result.getProblems().stream()
                            .<String>map(Problem::toString)
                            .collect(Collectors.joining("\n"))
            );
        }

        Optional<PackageDeclaration> declaration = result.getResult().flatMap(CompilationUnit::getPackageDeclaration);
        if (declaration.isEmpty()) {
            return StringUtils.EMPTY;
        }

        return declaration.get().getName().asString();
    }

    public static String getSourceRoot(String content, String filePath) throws IOException {
        String packageName = getPackageName(content);
        if (StringUtils.isEmpty(packageName)) {
            return FilenameUtils.getBaseName(filePath);
        }

        String sourceRoot = StringUtils.substringBefore(
                filePath, StringUtils.replace(packageName, ".", "/"));

        if (sourceRoot == null || sourceRoot.equals(filePath)) {
            throw new IOException(resourceBundle.getString("bazel.parser.error"));
        }

        return sourceRoot;
    }

    public static String getSourceRoot(File file) throws IOException {
        return getSourceRoot(FileUtils.readFileToString(file, StandardCharsets.UTF_8), file.getPath());
    }
}
