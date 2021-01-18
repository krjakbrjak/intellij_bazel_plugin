package krjakbrjak.bazel.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JavaUtilsTest {
    @Test
    public void testReadingPackageName() {
        {
            String content = StringUtils.joinWith(
                    "\n",
                    "package krjakbrjak.bazel;",
                    "public class A {}"
            );
            Assertions.assertDoesNotThrow(() -> {
                String packageName = new JavaFile().getPackageName(content);
                Assertions.assertEquals("krjakbrjak.bazel", packageName);
            });
        }

        {
            String content = StringUtils.joinWith(
                    "\n",
                    "packagekrjakbrjak.bazel;",
                    "public class A {}"
            );
            Assertions.assertThrows(IOException.class, () -> new JavaFile().getPackageName(content));
        }

        {
            String content = StringUtils.joinWith(
                    "\n",
                    "public class A {}"
            );
            Assertions.assertDoesNotThrow(() -> {
                String packageName = new JavaFile().getPackageName(content);
                Assertions.assertEquals(StringUtils.EMPTY, packageName);
            });
        }
    }

    @Test
    public void testExtractionOfSourceRoot() throws IOException {
        String content = StringUtils.joinWith(
                "\n",
                "package krjakbrjak.bazel;",
                "public class A {}"
        );

        JavaUnit unit = mock(JavaUnit.class);
        when(unit.getPackageName(anyString())).thenReturn("krjakbrjak.bazel");

        Assertions.assertDoesNotThrow(() -> {
            String sourceRoot = new JavaFileProcessor().getSourceRoot(content, "src/krjakbrjak/bazel/Example.java", unit);
            Assertions.assertEquals("src/", sourceRoot);
        });

        Assertions.assertDoesNotThrow(() -> {
            String sourceRoot = new JavaFileProcessor().getSourceRoot(content, "src\\krjakbrjak\\bazel\\Example.java", unit);
            Assertions.assertEquals("src/", sourceRoot);
        });

        Assertions.assertThrows(
                IOException.class,
                () -> new JavaFileProcessor().getSourceRoot(content, "/bazel/Example.java", unit));

        Assertions.assertDoesNotThrow(() -> {
            String sourceRoot = new JavaFileProcessor().getSourceRoot(content, "krjakbrjak/bazel/Example.java", unit);
            Assertions.assertEquals("", sourceRoot);
        });
    }
}
