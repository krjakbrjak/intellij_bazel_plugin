package krjakbrjak.bazel;

import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class starts the process and reads (concurrently) its input and error streams.
 */
public final class CommandStream implements Handle<Result> {
    private final Process process;
    private final List<Pair<Boolean, String>> lines = Collections.synchronizedList(new ArrayList<>());
    private final List<CompletableFuture<Void>> streamReadingFuture = new ArrayList<>();
    /**
     * A future that is called when the process is finished/terminated.
     */
    private final CompletableFuture<Result> future;

    public CommandStream(ProcessBuilder processBuilder, CommandLogger logger) throws IOException {
        process = processBuilder.start();
        streamReadingFuture.add(readInputStreamAsync(process.getInputStream(), false, logger, lines));
        streamReadingFuture.add(readInputStreamAsync(process.getErrorStream(), true, logger, lines));
        future = process.onExit().thenApplyAsync(p -> {
            streamReadingFuture.forEach(CompletableFuture::join);
            int rc = p.exitValue();
            return new Result(
                    lines.stream()
                            .filter(item -> item.getLeft().equals(true))
                            .map(Pair::getRight)
                            .collect(Collectors.joining()),
                    lines.stream()
                            .filter(item -> item.getLeft().equals(false))
                            .map(Pair::getRight)
                            .collect(Collectors.toList()),
                    rc
            );
        });
    }

    /**
     * Reads (asynchronously) lines from the {@link java.io.InputStream} object.
     *
     * <p>If <code>stream</code> argument is {@code null} then completed future
     * is returned immediately.
     *
     * @param stream  {@link java.io.InputStream} object to read from.
     * @param isError Marks input stream as error/input.
     * @param logger  {@link krjakbrjak.bazel.CommandLogger} object receiving data from the stream.
     * @param buffer  A buffer where data from the stream is written to.
     * @return {@link java.util.concurrent.CompletableFuture} object.
     * @throws NullPointerException if {@code buffer} or {@code logger} are {@code null}.
     */
    public static CompletableFuture<Void> readInputStreamAsync(
            InputStream stream,
            boolean isError,
            CommandLogger logger,
            List<Pair<Boolean, String>> buffer
    ) {
        if (buffer == null) {
            throw new NullPointerException("'buffer' must not be null.");
        }

        if (logger == null) {
            throw new NullPointerException("'logger' must not be null.");
        }

        if (stream == null) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.runAsync(() -> {
            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.add(Pair.of(isError, line));
                    logger.write(line, isError);
                }
            } catch (IOException e) {
                logger.write(e.getLocalizedMessage(), true);
            }
        });
    }

    @Override
    public CompletableFuture<Result> onExit() {
        return future;
    }

    @Override
    public void cancel(boolean force) {
        if (force) {
            process.destroyForcibly();
        } else {
            process.destroy();
        }
    }
}
