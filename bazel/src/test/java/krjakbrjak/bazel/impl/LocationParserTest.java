package krjakbrjak.bazel.impl;

import krjakbrjak.bazel.Kind;
import krjakbrjak.bazel.LocationParser;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class LocationParserTest {
    private static LocationParser parser;

    @BeforeAll
    public static void initAll() {
        parser = new LocationParserImpl();
    }

    static Stream<Arguments> provideOutput() {
        // Sample outputs were obtained with the command:
        // bazel query 'kind("source file", deps([label]))' --output location --noimplicit_deps
        return Stream.of(
                Arguments.arguments(
                        "/private/var/tmp/_bazel_nv/649aeb6130a877217ce2efa21e77857d/external/maven/BUILD:21:9: source file @maven//:v1/https/jcenter.bintray.com/colt/colt/1.2.0/colt-1.2.0.jar",
                        Kind.SOURCE_FILE,
                        "/private/var/tmp/_bazel_nv/649aeb6130a877217ce2efa21e77857d/external/maven/v1/https/jcenter.bintray.com/colt/colt/1.2.0/colt-1.2.0.jar"
                ),
                Arguments.arguments(
                        "/test_project/BUILD:3:1: source file //:src/main/java/com/example/ProjectRunner.java",
                        Kind.SOURCE_FILE,
                        "/test_project/src/main/java/com/example/ProjectRunner.java"
                ),
                Arguments.arguments(
                        "/test_project/src/main/java/com/example/cmdline/BUILD:3:1: source file //src/main/java/com/example/cmdline:Runner.java",
                        Kind.SOURCE_FILE,
                        "/test_project/src/main/java/com/example/cmdline/Runner.java"
                ),
                Arguments.arguments(
                        "BUILD:3:1: source file //src/main/java/com/example/cmdline:Runner.java",
                        Kind.SOURCE_FILE,
                        StringUtils.EMPTY
                ),
                Arguments.arguments(
                        "/BUILD:3:1: source file //src/main/java/com/example/cmdline:Runner.java",
                        Kind.SOURCE_FILE,
                        StringUtils.EMPTY
                ),
                Arguments.arguments(
                        StringUtils.EMPTY,
                        Kind.SOURCE_FILE,
                        StringUtils.EMPTY
                ),
                Arguments.arguments(
                        "abcdef",
                        Kind.SOURCE_FILE,
                        StringUtils.EMPTY
                ),
                Arguments.arguments(
                        null,
                        Kind.SOURCE_FILE,
                        StringUtils.EMPTY
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideOutput")
    public void testLocationParser(String output, Kind kind, String expected) {
        String actual = parser.parseLocation(output, kind);
        Assertions.assertEquals(expected, actual);
    }
}
