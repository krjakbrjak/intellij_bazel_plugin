package krjakbrjak.bazel;

import java.util.Collection;

public class Result {
    private final String error;
    private final Collection<String> output;
    private final int rc;

    public Result(String error, Collection<String> output, int rc) {
        this.error = error;
        this.output = output;
        this.rc = rc;
    }

    public String getError() {
        return error;
    }

    public Collection<String> getOutput() {
        return output;
    }

    public int getReturnCode() {
        return rc;
    }
}
