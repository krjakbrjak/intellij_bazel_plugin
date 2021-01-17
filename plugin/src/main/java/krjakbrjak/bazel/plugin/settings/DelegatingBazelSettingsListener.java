package krjakbrjak.bazel.plugin.settings;

import com.intellij.openapi.externalSystem.service.execution.ProgressExecutionMode;
import com.intellij.openapi.externalSystem.settings.DelegatingExternalSystemSettingsListener;
import com.intellij.openapi.externalSystem.settings.ExternalSystemSettingsListener;
import com.intellij.openapi.externalSystem.util.ExternalSystemUtil;
import com.intellij.openapi.project.Project;
import krjakbrjak.bazel.plugin.util.BazelConstants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DelegatingBazelSettingsListener extends DelegatingExternalSystemSettingsListener<BazelProjectSettings> implements @NotNull BazelSettingsListener {
    private static final Map<String, String> targetMap = new HashMap<>();
    private final Project project;

    public DelegatingBazelSettingsListener(
            @NotNull ExternalSystemSettingsListener<BazelProjectSettings> delegate,
            Project project) {
        super(delegate);
        this.project = project;
    }

    @Override
    public void onBazelExecutableChanged(
            String linkedProjectPath,
            List<String> oldExecutable,
            List<String> newExecutable) {
    }

    @Override
    public void onTargetChanged(String linkedProjectPath, int oldTarget, int newTarget) {
        String target = targetMap.getOrDefault(linkedProjectPath, null);
        List<String> targets = BazelSettings.getInstance(project)
                .getLinkedProjectSettings(linkedProjectPath)
                .getTargets();
        if (newTarget > -1 && targets != null && targets.size() > newTarget) {
            String newTargetName = targets.get(newTarget);
            if (!Objects.equals(target, newTargetName) && newTargetName != null) {
                targetMap.put(linkedProjectPath, newTargetName);
                ExternalSystemUtil.refreshProject(
                        project,
                        BazelConstants.SYSTEM_ID,
                        linkedProjectPath,
                        false,
                        ProgressExecutionMode.IN_BACKGROUND_ASYNC
                );
            }
        }
    }
}
