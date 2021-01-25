package krjakbrjak.bazel.plugin.project.services;

import com.intellij.openapi.externalSystem.service.execution.ExternalSystemJdkUtil;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.util.concurrency.EdtExecutorService;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class JdkResolverImpl implements JdkResolver {
    @Override
    public Sdk resolveJdk(String javaHome) {
        CompletableFuture<Sdk> completableFuture = new CompletableFuture<>();
        EdtExecutorService.getInstance().execute(() -> {
            Sdk[] sdks = ProjectJdkTable.getInstance().getAllJdks();
            for (Sdk sdk : sdks) {
                if (Objects.equals(sdk.getHomePath(), javaHome)) {
                    completableFuture.complete(sdk);
                    return;
                }
            }
            Sdk sdk = ExternalSystemJdkUtil.addJdk(javaHome);
            completableFuture.complete(sdk);
        });
        return completableFuture.join();
    }
}
