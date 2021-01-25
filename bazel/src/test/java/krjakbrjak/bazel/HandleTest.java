package krjakbrjak.bazel;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HandleTest {
    @Test
    public void testCancellation() {
        Handle<Result> handle = (Handle<Result>) mock(Handle.class);
        when(handle.onExit())
                .thenReturn(CompletableFuture.completedFuture(new Result("", List.of(), -200)));
        Handle<String> handle1 = new ConvertibleHandle<>(handle, result -> "handle1");

        Handle<String> handle2 = new ConvertibleHandle<>(handle, result -> "handle2");

        List<String> result = new ArrayList<>();
        handle1.onExit().thenApply(result::add);
        handle1.onExit().thenApply(result::add);
        handle2.onExit().thenApply(result::add);

        assertEquals(3, result.size());
        assertEquals(2, result.stream().filter(str -> str.equals("handle1")).count());
        assertEquals(1, result.stream().filter(str -> str.equals("handle2")).count());
    }
}
