package krjakbrjak.bazel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorkspaceTest {
    private BazelCommands workspace;

    @BeforeAll
    public void setUp() {
        workspace = new Workspace();
    }

    @Test
    public void testFailingCommand() throws ExecutionException, InterruptedException {
        ExecutableContext library = mock(ExecutableContext.class);
        Executable executable = mock(Executable.class);
        LocationParser locationParser = mock(LocationParser.class);

        when(library.getExecutable()).thenReturn(executable);
        when(library.getLocationParser()).thenReturn(locationParser);
        when(executable.run(anyString(), anyList()))
                .thenReturn(CompletableFuture.completedFuture(new Result("Failed", List.of(), -1)));

        Result result = workspace
                .queryAllDependencies(library, "WORKSPACE", "TARGET").get();

        verify(executable, times(1)).run(anyString(), anyList());
        verifyNoInteractions(locationParser);
        Assertions.assertEquals(-1, result.getReturnCode());
        Assertions.assertEquals("Failed", result.getError());

        result = workspace
                .queryLocalJdk(library, "WORKSPACE").get();

        verify(executable, times(2)).run(anyString(), anyList());
        verifyNoInteractions(locationParser);
        Assertions.assertEquals(-1, result.getReturnCode());
        Assertions.assertEquals("Failed", result.getError());
    }

    @Test
    public void testSucceedingCommand() throws ExecutionException, InterruptedException {
        ExecutableContext library = mock(ExecutableContext.class);
        Executable executable = mock(Executable.class);
        LocationParser locationParser = mock(LocationParser.class);

        when(library.getExecutable()).thenReturn(executable);
        when(library.getLocationParser()).thenReturn(locationParser);
        Collection<String> locations = List.of(
                "/src/main/java/pkg/BUILD:3:1: source file //src/main/java/pkg:Runner.java",
                "/src/main/java/pkg/BUILD:1: source file //src/main/java/pkg:BUILD",
                "/BUILD:3:1: source file //:src/pkg/Greeting.java"
        );
        when(executable.run(anyString(), anyList()))
                .thenReturn(CompletableFuture.completedFuture(new Result("", locations, 0)));
        when(locationParser.parseLocation(anyString(), any(Kind.class)))
                .thenReturn(returnsFirstArg().toString());

        Result result = workspace
                .queryAllDependencies(library, "WORKSPACE", "TARGET").get();

        verify(executable, times(1)).run(anyString(), anyList());
        verify(locationParser, times(locations.size())).parseLocation(anyString(), any(Kind.class));
        Assertions.assertEquals(0, result.getReturnCode());
        Assertions.assertTrue(result.getError().isEmpty());

        when(executable.run(anyString(), anyList()))
                .thenReturn(CompletableFuture.completedFuture(new Result("", List.of(""), 0)));

        result = workspace
                .queryLocalJdk(library, "WORKSPACE").get();

        verify(executable, times(2)).run(anyString(), anyList());
        verify(locationParser, times(locations.size() + 1)).parseLocation(anyString(), any(Kind.class));
        Assertions.assertEquals(0, result.getReturnCode());
        Assertions.assertTrue(result.getError().isEmpty());
    }
}
