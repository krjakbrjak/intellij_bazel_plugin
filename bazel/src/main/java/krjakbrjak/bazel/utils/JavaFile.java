package krjakbrjak.bazel.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaFile implements JavaUnit {
    @Override
    public String getPackageName(String content) throws IOException {
        JavaParser parser = new JavaParser();
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
}
