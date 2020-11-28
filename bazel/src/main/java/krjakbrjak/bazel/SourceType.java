package krjakbrjak.bazel;

import java.util.Collection;
import java.util.List;

/**
 * Represents the type of source.
 */
public enum SourceType {
    LIBRARY(List.of("jar")),
    SOURCE_CODE(List.of("java")),
    UNKNOWN(List.of());

    private final Collection<String> extensions;

    SourceType(Collection<String> extensions) {
        this.extensions = extensions;
    }

    /**
     * Helper method that returns a type of the source based on the filepath/name.
     *
     * @param filePath A path to the file (or its name).
     * @return {@link krjakbrjak.bazel.SourceType}
     */
    public static SourceType getSourceType(String filePath) {
        if (SOURCE_CODE.getExtensions().stream().anyMatch(filePath::endsWith)) {
            return SOURCE_CODE;
        }

        if (LIBRARY.getExtensions().stream().anyMatch(filePath::endsWith)) {
            return LIBRARY;
        }

        return UNKNOWN;
    }

    public Collection<String> getExtensions() {
        return extensions;
    }
}
