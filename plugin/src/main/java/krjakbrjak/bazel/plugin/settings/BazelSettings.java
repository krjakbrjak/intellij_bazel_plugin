package krjakbrjak.bazel.plugin.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.externalSystem.settings.AbstractExternalSystemSettings;
import com.intellij.openapi.externalSystem.settings.ExternalSystemSettingsListener;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.annotations.XCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@State(name = "BazelSettings", storages = @Storage("bazel.xml"))
public class BazelSettings extends
        AbstractExternalSystemSettings<BazelSettings, BazelProjectSettings, BazelSettingsListener>
        implements PersistentStateComponent<BazelSettings.MyState> {
    protected BazelSettings(@NotNull Project project) {
        super(BazelSettingsListener.TOPIC, project);
    }

    @NotNull
    public static BazelSettings getInstance(@NotNull Project project) {
        return project.getService(BazelSettings.class);
    }

    @Override
    public void subscribe(@NotNull ExternalSystemSettingsListener<BazelProjectSettings> listener) {
        doSubscribe(new DelegatingBazelSettingsListener(listener, getProject()), getProject());
    }

    @Override
    protected void copyExtraSettingsFrom(@NotNull BazelSettings settings) {
    }

    @Override
    protected void checkSettings(@NotNull BazelProjectSettings old, @NotNull BazelProjectSettings current) {
        if (!Objects.equals(old.getExecutable(), current.getExecutable())) {
            getPublisher().onBazelExecutableChanged(getProject().getBasePath(), old.getExecutable(), current.getExecutable());
        }
    }

    @Override
    public @Nullable BazelSettings.MyState getState() {
        MyState state = new MyState();
        fillState(state);

        return state;
    }

    @Override
    public void loadState(@NotNull MyState state) {
        super.loadState(state);
    }

    public static class MyState implements State<BazelProjectSettings> {
        private final Set<BazelProjectSettings> projectSettings = new HashSet<>();

        @Override
        @XCollection(elementTypes = BazelProjectSettings.class)
        public Set<BazelProjectSettings> getLinkedExternalProjectsSettings() {
            return projectSettings;
        }

        @Override
        public void setLinkedExternalProjectsSettings(Set<BazelProjectSettings> settings) {
            if (settings != null) {
                projectSettings.addAll(settings);
            }
        }
    }
}
