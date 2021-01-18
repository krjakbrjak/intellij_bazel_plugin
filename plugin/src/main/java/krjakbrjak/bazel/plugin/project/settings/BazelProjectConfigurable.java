package krjakbrjak.bazel.plugin.project.settings;

import com.intellij.openapi.externalSystem.service.settings.AbstractExternalSystemConfigurable;
import com.intellij.openapi.externalSystem.util.ExternalSystemSettingsControl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import krjakbrjak.bazel.plugin.settings.BazelProjectSettings;
import krjakbrjak.bazel.plugin.settings.BazelSettings;
import krjakbrjak.bazel.plugin.settings.BazelSettingsListener;
import krjakbrjak.bazel.plugin.util.BazelConstants;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ResourceBundle;

public class BazelProjectConfigurable extends AbstractExternalSystemConfigurable<BazelProjectSettings, BazelSettingsListener, BazelSettings> {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("/Ui");

    public BazelProjectConfigurable(@NotNull Project project) {
        super(project, BazelConstants.SYSTEM_ID);
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return resourceBundle.getString("bazel.projectProperties");
    }

    @Override
    protected @NotNull ExternalSystemSettingsControl<BazelProjectSettings> createProjectSettingsControl(@NotNull BazelProjectSettings settings) {
        return new BazelProjectSettingsControl(settings);
    }

    @Override
    protected @Nullable ExternalSystemSettingsControl<BazelSettings> createSystemSettingsControl(@NotNull BazelSettings settings) {
        return null;
    }

    @Override
    protected @NotNull BazelProjectSettings newProjectSettings() {
        return new BazelProjectSettings();
    }

    @Override
    public @NotNull
    @NonNls
    String getId() {
        return getDisplayName();
    }
}
