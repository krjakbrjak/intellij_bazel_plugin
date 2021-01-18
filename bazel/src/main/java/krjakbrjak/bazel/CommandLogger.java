package krjakbrjak.bazel;

public interface CommandLogger {
    void write(String line, boolean isError);
}
