package krjakbrjak.bazel.plugin;

import krjakbrjak.bazel.Context;
import krjakbrjak.bazel.Executable;
import krjakbrjak.bazel.ExecutableContext;
import krjakbrjak.bazel.Library;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LibraryTestImpl implements Library {
    private final Context ctx = mock(Context.class);
    private final ExecutableContext.Builder builder = mock(ExecutableContext.Builder.class);
    private final ExecutableContext execCtx = mock(ExecutableContext.class);

    public LibraryTestImpl() {
        super();
        when(ctx.getExecutableBuilder()).thenReturn(builder);
        when(builder.withCommand(any())).thenReturn(builder);
        when(builder.build()).thenReturn(execCtx);
        when(execCtx.getExecutable()).thenReturn(mockExecutable());
    }

    private Executable mockExecutable() {
        Executable exe = mock(Executable.class);
        return exe;
    }

    @Override
    public Context getContext() {
        return ctx;
    }
}
