package krjakbrjak.bazel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class SourceTypeTest {
    static Stream<Arguments> providesSources() {
        return Stream.of(
                Arguments.arguments("*.java", SourceType.SOURCE_CODE),
                Arguments.arguments("*.cpp", SourceType.UNKNOWN),
                Arguments.arguments("*.jar", SourceType.LIBRARY),
                Arguments.arguments("*.kt", SourceType.UNKNOWN)
        );
    }

    @ParameterizedTest
    @MethodSource("providesSources")
    public void testSourceTypes(String filePath, SourceType sourceType) {
        Assertions.assertEquals(sourceType, SourceType.getSourceType(filePath));
    }
}
