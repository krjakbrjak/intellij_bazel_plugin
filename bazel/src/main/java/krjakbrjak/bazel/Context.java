package krjakbrjak.bazel;

import dagger.Component;
import dagger.Module;

import javax.inject.Singleton;

@Singleton
@Component(modules = ExecutableModule.class)
public interface Context {
    ExecutableContext.Builder getExecutableBuilder();
}

@Module(subcomponents = ExecutableContext.class)
class ExecutableModule {
}
