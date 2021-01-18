package krjakbrjak.bazel.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * An interface that represents a java unit.
 */
public interface JavaUnit {
    default String getPackageName(File file) throws IOException {
        return getPackageName(
                FileUtils.readFileToString(file, StandardCharsets.UTF_8)
        );
    }

    /**
     * Parses {@code content} and extracts java package name.
     *
     * @param content A content of a java unit.
     * @return A name of the java package.
     * @throws IOException If the content cannot be parsed.
     */
    String getPackageName(String content) throws IOException;
}
