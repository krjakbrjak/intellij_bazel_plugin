package krjakbrjak.bazel;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Subcomponent(modules = TestExecutable.class)
interface TestExecutableContext {
    Executable getExecutable();

    @Subcomponent.Builder
    interface Builder {
        TestExecutableContext build();
    }
}

@Singleton
@Component(modules = TestExecutableModule.class)
public interface TestContext {
    TestExecutableContext.Builder getExecutableBuilder();
}

@Module
class TestExecutable implements Executable {
    @Provides
    public static Executable create() {
        return new TestExecutable();
    }

    @Override
    public CompletableFuture<Result> run(String workspacePath, Collection<String> options, CommandLogger logger) {
        return CompletableFuture.supplyAsync(() -> new Result(null, options, 0));
    }

    @Override
    public Collection<String> getCommand() {
        return List.of("test");
    }
}

@Module(subcomponents = TestExecutableContext.class)
class TestExecutableModule {
}
