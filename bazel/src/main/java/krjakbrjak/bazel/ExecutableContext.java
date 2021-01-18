package krjakbrjak.bazel;

import dagger.BindsInstance;
import dagger.Subcomponent;
import krjakbrjak.bazel.impl.ContextProvider;

import java.util.Collection;

@Subcomponent(modules = ContextProvider.class)
public interface ExecutableContext {
    Executable getExecutable();

    LocationParser getLocationParser();

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        Builder withCommand(@Executes Collection<String> command);

        ExecutableContext build();
    }
}
