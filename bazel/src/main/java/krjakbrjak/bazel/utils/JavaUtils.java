package krjakbrjak.bazel.utils;

import java.io.File;
import java.io.IOException;

public class JavaUtils {
    public static String getSourceSet(String filePath) throws IOException {
        return new JavaFileProcessor().getSourceRoot(new File(filePath), new JavaFile());
    }
}
