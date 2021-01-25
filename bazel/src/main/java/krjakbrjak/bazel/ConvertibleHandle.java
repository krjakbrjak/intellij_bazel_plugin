package krjakbrjak.bazel;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * A helper handle class that allows to create handles of different types
 * to the same process.
 *
 * @param <O> Original type.
 * @param <E> New type.
 */
public class ConvertibleHandle<O, E> implements Handle<E> {
    private final Handle<O> original;
    private final CompletableFuture<E> future;

    public ConvertibleHandle(Handle<O> original, Function<O, E> conversion) {
        this.original = original;
        future = this.original.onExit().thenApplyAsync(conversion);
    }

    @Override
    public CompletableFuture<E> onExit() {
        return future;
    }

    @Override
    public void cancel(boolean force) {
        original.cancel(force);
    }
}
