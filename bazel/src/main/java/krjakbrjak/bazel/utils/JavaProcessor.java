package krjakbrjak.bazel.utils;

import java.io.IOException;

/**
 * An interface that helps to get more information about the java sources
 * based on the content of java units and their locations.
 */
public interface JavaProcessor {
    /**
     * Processes a {@code content} and {@code filePath} and returns a path (with Unix file
     * path separator '/') to the source root.
     *
     * @param filePath A path to the java unit.
     * @param unit     {@link krjakbrjak.bazel.utils.JavaUnit} object.
     * @return A path to the source root.
     * @throws IOException If the content cannot be parsed.
     */
    String getSourceRoot(String filePath, JavaUnit unit) throws IOException;
}
