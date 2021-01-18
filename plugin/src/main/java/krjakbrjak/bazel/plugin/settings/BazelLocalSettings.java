package krjakbrjak.bazel.plugin.settings;

import com.intellij.openapi.components.*;
import com.intellij.openapi.externalSystem.settings.AbstractExternalSystemLocalSettings;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.ContainerUtil;
import krjakbrjak.bazel.plugin.util.BazelConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@State(name = "BazelLocalSettings", storages = @Storage(StoragePathMacros.CACHE_FILE))
public class BazelLocalSettings extends AbstractExternalSystemLocalSettings<BazelLocalSettings.MyState>
        implements PersistentStateComponent<BazelLocalSettings.MyState> {
    protected BazelLocalSettings(@NotNull Project project) {
        super(BazelConstants.SYSTEM_ID, project, new MyState());
    }

    @NotNull
    public static BazelLocalSettings getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, BazelLocalSettings.class);
    }

    @Nullable
    public List<String> getExecutable(@NotNull String linkedProjectPath) {
        return ContainerUtil.notNullize(state.myBazelExecutables).get(linkedProjectPath);
    }

    public void setExecutable(@NotNull String linkedProjectPath, @NotNull List<String> executable) {
        state.myBazelExecutables.put(linkedProjectPath, executable);
    }

    @Override
    public void forgetExternalProjects(@NotNull Set<String> linkedProjectPathsToForget) {
        super.forgetExternalProjects(linkedProjectPathsToForget);
        for (String path : linkedProjectPathsToForget) {
            state.myBazelExecutables.remove(path);
        }
    }

    @Override
    public void loadState(@NotNull MyState state) {
        super.loadState(state);
    }

    public static class MyState extends AbstractExternalSystemLocalSettings.State {
        public Map<String, List<String>> myBazelExecutables = new HashMap<>();
    }
}
