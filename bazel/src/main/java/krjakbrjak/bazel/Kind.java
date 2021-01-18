package krjakbrjak.bazel;

import java.util.ResourceBundle;

/**
 * Represents an extra information on how an output
 * from <code>bazel query ... --output location</code>
 * to be parsed. See {@link krjakbrjak.bazel.LocationParser}.
 */
public enum Kind {
    SOURCE_FILE("bazel.output.location.sourceFile"),
    JAVA_RUNTIME_RULE("bazel.output.location.javaRuntimeRule");

    private static final ResourceBundle resource = ResourceBundle.getBundle("Bazel");
    private final String kind;

    Kind(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return resource.getString(kind);
    }
}
