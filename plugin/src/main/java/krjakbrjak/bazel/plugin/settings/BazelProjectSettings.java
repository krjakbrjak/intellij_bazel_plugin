package krjakbrjak.bazel.plugin.settings;

import com.intellij.openapi.externalSystem.settings.ExternalProjectSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class BazelProjectSettings extends ExternalProjectSettings {
    @Nullable
    private List<String> executable;
    @Nullable
    private List<String> targets;
    private int currentTarget = -1;
    @Nullable
    private List<String> packages;
    private int currentPackage = -1;

    @Nullable
    public List<String> getExecutable() {
        return executable;
    }

    public void setExecutable(@Nullable List<String> executable) {
        this.executable = executable;
    }

    @Nullable
    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(@Nullable List<String> targets) {
        this.targets = targets;
    }

    public int getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(int currentTarget) {
        this.currentTarget = currentTarget;
    }

    @Nullable
    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(@Nullable List<String> packages) {
        this.packages = packages;
    }

    public int getCurrentPackage() {
        return currentPackage;
    }

    public void setCurrentPackage(int currentPackage) {
        this.currentPackage = currentPackage;
    }

    @Override
    public @NotNull ExternalProjectSettings clone() {
        BazelProjectSettings settings = new BazelProjectSettings();
        copyTo(settings);

        settings.setExecutable(getExecutable());
        settings.setTargets(getTargets());
        settings.setCurrentTarget(getCurrentTarget());
        settings.setPackages(getPackages());
        settings.setCurrentPackage(getCurrentPackage());

        return settings;
    }

    @Override
    public boolean equals(Object o) {
        BazelProjectSettings settings = (BazelProjectSettings) o;
        return super.equals(o) &&
                Objects.equals(getExecutable(), settings.getExecutable()) &&
                Objects.equals(getTargets(), settings.getTargets()) &&
                Objects.equals(getCurrentTarget(), settings.getCurrentTarget()) &&
                Objects.equals(getPackages(), settings.getPackages()) &&
                Objects.equals(getCurrentPackage(), settings.getCurrentPackage());
    }
}
