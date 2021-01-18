package krjakbrjak.bazel.plugin.services;

import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.testFramework.IdeaTestUtil;
import krjakbrjak.bazel.plugin.project.services.JdkResolver;

public class JdkResolverTest implements JdkResolver {
    @Override
    public Sdk resolveJdk(String javaHome) {
        return IdeaTestUtil.createMockJdk("test jdk", javaHome);
    }
}
