package krjakbrjak.bazel.plugin.project.settings;

import com.intellij.openapi.externalSystem.service.settings.AbstractImportFromExternalSystemControl;
import com.intellij.openapi.externalSystem.util.ExternalSystemSettingsControl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import krjakbrjak.bazel.plugin.settings.BazelProjectSettings;
import krjakbrjak.bazel.plugin.settings.BazelSettings;
import krjakbrjak.bazel.plugin.settings.BazelSettingsListener;
import krjakbrjak.bazel.plugin.util.BazelConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImportFromBazelControl extends AbstractImportFromExternalSystemControl<BazelProjectSettings, BazelSettingsListener, BazelSettings> {
    public ImportFromBazelControl() {
        super(
                BazelConstants.SYSTEM_ID,
                BazelSettings.getInstance(ProjectManager.getInstance().getDefaultProject()),
                new BazelProjectSettings()
        );
    }

    @Override
    public void setCurrentProject(@Nullable Project currentProject) {
        super.setCurrentProject(currentProject);
        ((BazelProjectSettingsControl) getProjectSettingsControl()).setCurrentProject(currentProject);
    }

    @Override
    protected void onLinkedProjectPathChange(@NotNull String path) {
    }

    @Override
    protected @NotNull ExternalSystemSettingsControl<BazelProjectSettings> createProjectSettingsControl(@NotNull BazelProjectSettings settings) {
        return new BazelProjectSettingsControl(settings);
    }

    @Override
    protected @Nullable ExternalSystemSettingsControl<BazelSettings> createSystemSettingsControl(@NotNull BazelSettings settings) {
        return null;
    }
}
