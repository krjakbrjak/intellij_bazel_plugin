package krjakbrjak.bazel.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * An interface that helps to get more information about the java sources
 * based on the content of java units and their locations.
 */
public interface JavaProcessor {
    /**
     * Processes a {@code content} and {@code filePath} and returns a path (with Unix file
     * path separator '/') to the source root.
     *
     * @param content  A content of the java unit.
     * @param filePath A path to the java unit.
     * @param unit     {@link krjakbrjak.bazel.utils.JavaUnit} object.
     * @return A path to the source root.
     * @throws IOException If the content cannot be parsed.
     */
    String getSourceRoot(String content, String filePath, JavaUnit unit) throws IOException;

    default String getSourceRoot(File file, JavaUnit unit) throws IOException {
        return getSourceRoot(FileUtils.readFileToString(file, StandardCharsets.UTF_8), file.getPath(), unit);
    }
}
