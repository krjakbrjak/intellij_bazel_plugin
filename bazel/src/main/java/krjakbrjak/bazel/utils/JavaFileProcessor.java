package krjakbrjak.bazel.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ResourceBundle;

public class JavaFileProcessor implements JavaProcessor {
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle("Bazel");

    @Override
    public String getSourceRoot(String content, String filePath, JavaUnit unit) throws IOException {
        String packageName = unit.getPackageName(content);
        if (StringUtils.isEmpty(packageName)) {
            return FilenameUtils.getBaseName(filePath);
        }

        String unixFilePath = FilenameUtils.separatorsToUnix(filePath);
        String sourceRoot = StringUtils.substringBefore(
                unixFilePath, StringUtils.replace(packageName, ".", "/"));

        if (sourceRoot == null || sourceRoot.equals(unixFilePath)) {
            throw new IOException(resourceBundle.getString("bazel.parser.error"));
        }

        return sourceRoot;
    }
}
