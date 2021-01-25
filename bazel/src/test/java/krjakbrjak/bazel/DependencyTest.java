package krjakbrjak.bazel;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DependencyTest {
    @Test
    public void testConstruction() {
        final List<String> filePaths = List.of("a.java", "b.c", "c.jar");
        final List<SourceType> fileTypes = List.of(SourceType.SOURCE_CODE, SourceType.UNKNOWN, SourceType.LIBRARY);
        assertEquals(
                fileTypes, filePaths.stream()
                        .map(Dependency::new)
                        .map(Dependency::getSourceType)
                        .collect(Collectors.toList())
        );
    }

    @Test
    public void testEquality() {
        var dep1 = new Dependency("a.java");
        var dep2 = new Dependency("a.java");
        var dep3 = new Dependency("a.jara");
        assertEquals(dep1, dep1);
        assertEquals(dep1, dep2);
        assertNotEquals(dep1, dep3);
        assertNotEquals(dep2, dep3);
    }
}
