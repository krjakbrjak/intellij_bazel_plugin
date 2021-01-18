package krjakbrjak.bazel.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JavaParserTest {
    @Test
    public void testReadingPackageName() {
        {
            String content = StringUtils.joinWith(
                    "\n",
                    "package krjakbrjak.bazel;",
                    "public class A {}"
            );
            Assertions.assertDoesNotThrow(() -> {
                String packageName = JavaParser.getPackageName(content);
                Assertions.assertEquals("krjakbrjak.bazel", packageName);
            });
        }

        {
            String content = StringUtils.joinWith(
                    "\n",
                    "packagekrjakbrjak.bazel;",
                    "public class A {}"
            );
            Assertions.assertThrows(IOException.class, () -> JavaParser.getPackageName(content));
        }

        {
            String content = StringUtils.joinWith(
                    "\n",
                    "public class A {}"
            );
            Assertions.assertDoesNotThrow(() -> {
                String packageName = JavaParser.getPackageName(content);
                Assertions.assertEquals(StringUtils.EMPTY, packageName);
            });
        }
    }

    @Test
    public void testExtractionOfSourceRoot() {
        String content = StringUtils.joinWith(
                "\n",
                "package krjakbrjak.bazel;",
                "public class A {}"
        );

        Assertions.assertDoesNotThrow(() -> {
            String sourceRoot = JavaParser.getSourceRoot(content, "src/krjakbrjak/bazel/Example.java");
            Assertions.assertEquals("src/", sourceRoot);
        });

        Assertions.assertThrows(
                IOException.class,
                () -> JavaParser.getSourceRoot(content, "/bazel/Example.java"));

        Assertions.assertDoesNotThrow(() -> {
            String sourceRoot = JavaParser.getSourceRoot(content, "krjakbrjak/bazel/Example.java");
            Assertions.assertEquals("", sourceRoot);
        });
    }
}
