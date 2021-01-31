package krjakbrjak.bazel.tasks;

public enum BazelTaskNames {
    BUILD("build"),
    RUN("run"),
    CLEAN("clean");

    private final String name;

    BazelTaskNames(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
