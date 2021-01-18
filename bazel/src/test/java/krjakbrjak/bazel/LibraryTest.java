package krjakbrjak.bazel;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LibraryTest {
    private static TestContext ctx;

    @BeforeAll
    public static void initAll() {
        ctx = DaggerTestContext.create();
    }

    @AfterAll
    public static void deinitAll() {
        ctx = null;
    }

    @Test
    public void testVersion() throws ExecutionException, InterruptedException {
        CompletableFuture<Result> result = new CompletableFuture<>();
        ctx.getExecutableBuilder().build().getExecutable()
                .run("WORKSPACE", List.of("QUERY"))
                .thenAccept(result::complete);
        assertEquals("QUERY", String.join(":", result.get().getOutput()));
    }
}
