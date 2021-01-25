package krjakbrjak.bazel;

import java.util.concurrent.CompletableFuture;

/**
 * An interface that represents the running process.
 *
 * @param <E> A type of object the handle gets from the running process.
 */
public interface Handle<E> {
    /**
     * Retuns a {@link java.util.concurrent.CompletableFuture} that holds a result.
     *
     * @return {@code CompletableFuture<T>} object.
     */
    CompletableFuture<E> onExit();

    /**
     * Cancels the running process.
     *
     * @param force Defines if a process must be forcibly terminated.
     */
    void cancel(boolean force);
}
