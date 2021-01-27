package krjakbrjak.bazel;

import java.util.Objects;

public class SourceSet {
    private final String path;
    private final String pkg;

    public SourceSet(String path, String pkg) {
        this.path = path;
        this.pkg = pkg;
    }

    public String getPath() {
        return path;
    }

    public String getPackage() {
        return pkg;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        SourceSet other = (SourceSet) obj;
        return Objects.equals(this.getPath(), other.getPath()) ||
                Objects.equals(this.getPackage(), other.getPackage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath(), getPackage());
    }
}
