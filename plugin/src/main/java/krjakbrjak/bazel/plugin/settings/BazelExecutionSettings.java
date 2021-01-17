package krjakbrjak.bazel.plugin.settings;

import com.intellij.openapi.externalSystem.model.settings.ExternalSystemExecutionSettings;
import com.sun.istack.Nullable;

import java.util.List;

public class BazelExecutionSettings extends ExternalSystemExecutionSettings {
    private final String target;
    private List<String> myExecutable = List.of();
    private String myIdeProjectPath;

    public BazelExecutionSettings(@Nullable List<String> executable, @Nullable String target) {
        if (executable != null) {
            myExecutable = executable;
        }
        this.target = target;
    }

    public List<String> getExecutable() {
        return myExecutable;
    }

    public String getTarget() {
        return target;
    }

    public String getIdeProjectPath() {
        return myIdeProjectPath;
    }

    public void setIdeProjectPath(@Nullable String ideProjectPath) {
        myIdeProjectPath = ideProjectPath;
    }
}
