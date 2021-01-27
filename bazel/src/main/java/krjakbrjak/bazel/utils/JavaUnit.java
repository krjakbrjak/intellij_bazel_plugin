package krjakbrjak.bazel.utils;

/**
 * An interface that represents a java unit.
 */
public interface JavaUnit {
    /**
     * Parses {@code content} and extracts java package name.
     *
     * @return A name of the java package.
     */
    String getPackageName();
}
