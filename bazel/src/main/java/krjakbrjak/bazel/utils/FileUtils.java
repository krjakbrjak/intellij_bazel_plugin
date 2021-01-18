package krjakbrjak.bazel.utils;

import java.io.IOException;
import java.nio.file.Paths;

public class FileUtils {
    public static String getRealPath(String path) throws IOException {
        return Paths.get(path).toRealPath().toString();
    }
}
