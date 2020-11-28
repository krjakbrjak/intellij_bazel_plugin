package krjakbrjak.bazel;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

public class CommandTest {
    @Test
    public void testCommand() throws ExecutionException, InterruptedException {
        var commandBuilder = new CommandBuilder()
                .command(List.of("laksjd"))
                .args("a")
                .args("b")
                .args(List.of("1", "2"))
                .workingDir("WORKINGDIR");
        Assertions.assertEquals(List.of("laksjd", "a", "b", "1", "2"), commandBuilder.getCommand());
        Assertions.assertEquals("WORKINGDIR", commandBuilder.getWorkingDir());

        CompletableFuture<Result> future = commandBuilder.exec((a, b) -> {
        });
        Result result = future.get();
        Assertions.assertNotEquals(0, result.getReturnCode());
        Assertions.assertTrue(result.getOutput().isEmpty());
    }

    @Test
    public void testCommandStreamReadStreamArguments() {
        CommandLogger logger = mock(CommandLogger.class);
        List<Pair<Boolean, String>> buffer = mock(List.class);

        CommandStream.readInputStreamAsync(null, false, logger, buffer).join();
        Mockito.verify(logger, Mockito.times(0)).write(anyString(), anyBoolean());

        assertThrows(NullPointerException.class,
                () -> CommandStream.readInputStreamAsync
                        (InputStream.nullInputStream(),
                                false,
                                logger,
                                null));

        assertThrows(NullPointerException.class,
                () -> CommandStream.readInputStreamAsync
                        (InputStream.nullInputStream(),
                                false,
                                null,
                                buffer));

        final String TEST_DATA = "test";
        InputStream stream = new ByteArrayInputStream(TEST_DATA.getBytes());
        List<Pair<Boolean, String>> data = new ArrayList<>();
        CommandStream.readInputStreamAsync(stream, false, logger, data).join();
        assertEquals(1, data.size());
        assertEquals(TEST_DATA, data.get(0).getRight());

        stream = new ByteArrayInputStream(String.format("%s\n%s", TEST_DATA, TEST_DATA).getBytes());
        data = new ArrayList<>();
        CommandStream.readInputStreamAsync(stream, false, logger, data).join();
        assertEquals(2, data.size());
        assertEquals(TEST_DATA, data.get(0).getRight());
        assertEquals(TEST_DATA, data.get(1).getRight());
    }
}
