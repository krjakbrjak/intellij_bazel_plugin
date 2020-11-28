package krjakbrjak.bazel;

public class LibraryImpl implements Library {
    private final Context ctx = DaggerContext.create();

    @Override
    public Context getContext() {
        return ctx;
    }
}
