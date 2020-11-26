package krjakbrjak.bazel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class LibraryTest {
    @Test public void testVersion() {
        assertNotEquals(null, Library.getVersion());
    }
}
