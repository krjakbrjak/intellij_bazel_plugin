package krjakbrjak.bazel.plugin.project.services;

import com.intellij.openapi.projectRoots.Sdk;

/**
 * An interface to resolve an {@link com.intellij.openapi.projectRoots.Sdk}.
 */
public interface JdkResolver {
    /**
     * Retrieves an jdk for a {@code javaHome}.
     *
     * @param javaHome A path to JDK.
     * @return {@link com.intellij.openapi.projectRoots.Sdk} object.
     */
    Sdk resolveJdk(String javaHome);
}
