package krjakbrjak.bazel;

/**
 * An interface for a parser of an output of
 * a bazel command:
 * <code>bazel query kind(..., ...) --output location</code>.
 */
public interface LocationParser {
    /**
     * Parses a line from <code>bazel query</code> output.
     *
     * @param output A line of output.
     * @param kind   {@link krjakbrjak.bazel.Kind} object.
     * @return Parsed string or an empty string if an error occurred.
     */
    String parseLocation(String output, Kind kind);
}
