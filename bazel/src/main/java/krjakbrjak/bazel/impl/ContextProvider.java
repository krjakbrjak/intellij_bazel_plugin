package krjakbrjak.bazel.impl;

import dagger.Module;
import dagger.Provides;
import krjakbrjak.bazel.Executable;
import krjakbrjak.bazel.Executes;
import krjakbrjak.bazel.LocationParser;

import java.util.Collection;

@Module
public class ContextProvider {
    @Provides
    public static LocationParser createParser() {
        return new LocationParserImpl();
    }

    @Provides
    public static Executable create(@Executes Collection<String> command) {
        return new ExecutableImpl(command);
    }
}
