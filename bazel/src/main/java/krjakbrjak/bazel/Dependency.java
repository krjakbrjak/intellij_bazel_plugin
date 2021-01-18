package krjakbrjak.bazel;

import krjakbrjak.bazel.utils.JavaUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * Holds an information about the file: its type ({@link krjakbrjak.bazel.SourceType})
 * and a pth to it.
 */
public class Dependency {
    private final SourceType sourceType;
    private final String path;

    // Factory would be better?
    public Dependency(String filePath) {
        sourceType = SourceType.getSourceType(filePath);
        path = filePath;
    }

    /**
     * Returns a source set for java source code.
     *
     * @param dependency {@link krjakbrjak.bazel.Dependency} object.
     * @return {@code String} or {@code null} if {@code dependency} is not {@code SourceType.SOURCE_CODE}.
     */
    public static String getSourceSet(Dependency dependency) {
        if (dependency.getSourceType().equals(SourceType.SOURCE_CODE)) {
            try {
                return JavaUtils.getSourceSet(dependency.getPath());
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (super.equals(o)) {
            return true;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        Dependency other = (Dependency) o;

        return Objects.equals(path, other.path) &&
                Objects.equals(sourceType, other.sourceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, sourceType);
    }
}
