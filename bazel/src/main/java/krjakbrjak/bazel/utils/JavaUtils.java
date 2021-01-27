package krjakbrjak.bazel.utils;

import java.io.File;
import java.io.IOException;

public class JavaUtils {
    public static String getSourceSet(String filePath) throws IOException {
        return new JavaFileProcessor().getSourceRoot(filePath, new JavaFile(new File(filePath)));
    }
}
