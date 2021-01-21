package krjakbrjak.bazel.plugin.project.settings;

import com.google.common.base.Objects;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.externalSystem.service.settings.AbstractExternalProjectSettingsControl;
import com.intellij.openapi.externalSystem.util.ExternalSystemUiUtil;
import com.intellij.openapi.externalSystem.util.PaintAwarePanel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.UIUtil;
import krjakbrjak.bazel.BazelCommands;
import krjakbrjak.bazel.CommandLogger;
import krjakbrjak.bazel.ExecutableContext;
import krjakbrjak.bazel.Library;
import krjakbrjak.bazel.plugin.project.BazelModuleSetup;
import krjakbrjak.bazel.plugin.project.BazelModuleSetupListener;
import krjakbrjak.bazel.plugin.settings.BazelProjectSettings;
import krjakbrjak.bazel.plugin.settings.BazelSettings;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BazelProjectSettingsControl extends AbstractExternalProjectSettingsControl<BazelProjectSettings> {
    private final BazelModuleSetup setup = BazelModuleSetup.getInstance();

    public BazelProjectSettingsControl(@NotNull BazelProjectSettings initialSettings) {
        super(initialSettings);
        setup.subscribeForChanges(new BazelModuleSetupListener() {
            @Override
            public void onBazelExecutableChanged(List<String> executable) {
                BazelProjectSettings settings = getInitialSettings();
                if (!Objects.equal(settings.getExecutable(), executable)) {
                    // Sets an executable and invalidates the rest of the settings
                    settings.setExecutable(executable);
                    settings.setPackages(null);
                    settings.setCurrentPackage(-1);
                    settings.setTargets(null);
                    settings.setCurrentTarget(-1);
                    ProgressDialog dialog = new ProgressDialog();
                    getPackages(settings, (line, isError) -> {})
                            .thenAcceptAsync(packages -> {
                                settings.setPackages(packages);
                                setup.setData(settings);
                            });
                }
            }

            @Override
            public void onTargetChanged(String target) {
                BazelProjectSettings settings = getInitialSettings();
                if (settings.getTargets() != null) {
                    int newCurrent = settings.getTargets().indexOf(target);
                    int oldCurrent = settings.getCurrentTarget();
                    if (newCurrent != oldCurrent) {
                        settings.setCurrentTarget(newCurrent);
                        if (getProject() != null) {
                            BazelSettings.getInstance(getProject())
                                    .getPublisher()
                                    .onTargetChanged(getProject().getBasePath(), oldCurrent, newCurrent);
                        }
                    }
                }
            }

            @Override
            public void onPackageChanged(String pkg) {
                BazelProjectSettings settings = getInitialSettings();
                // Invalidates targets
                settings.setTargets(null);
                settings.setCurrentTarget(-1);
                if (settings.getPackages() != null) {
                    int newCurrent = settings.getPackages().indexOf(pkg);
                    int oldCurrent = settings.getCurrentPackage();
                    if (newCurrent != oldCurrent) {
                        settings.setCurrentPackage(newCurrent);
                        getTargets(settings).thenAccept(targets -> {
                            settings.setTargets(targets);
                            setup.setData(settings);
                        });
                    }
                }
            }
        });
    }

    private static CompletableFuture<List<String>> getTargets(@NotNull BazelProjectSettings settings) {
        ExecutableContext exeCtx = ServiceManager.getService(Library.class)
                .getContext()
                .getExecutableBuilder()
                .withCommand(settings.getExecutable())
                .build();
        return ServiceManager.getService(BazelCommands.class).queryAllTargets(
                exeCtx,
                settings.getExternalProjectPath(),
                settings.getPackages().get(settings.getCurrentPackage()))
                .thenApplyAsync(result -> {
                    if (result.getReturnCode() == 0) {
                        return new ArrayList<>(result.getOutput());
                    }
                    return null;
                });
    }

    private static CompletableFuture<List<String>> getPackages(@NotNull BazelProjectSettings settings, CommandLogger logger) {
        ExecutableContext exeCtx = ServiceManager.getService(Library.class)
                .getContext()
                .getExecutableBuilder()
                .withCommand(settings.getExecutable())
                .build();
        return ServiceManager.getService(BazelCommands.class).queryAllPackages(
                exeCtx,
                settings.getExternalProjectPath(), logger)
                .thenApplyAsync(result -> {
                    if (result.getReturnCode() == 0) {
                        return new ArrayList<>(result.getOutput());
                    }
                    logger.write(result.getError(), true);
                    return null;
                });
    }

    @Override
    protected void fillExtraControls(@NotNull PaintAwarePanel content, int indentLevel) {
        GridBag constraints = ExternalSystemUiUtil.getFillLineConstraints(indentLevel);
        constraints.insets.top = UIUtil.LARGE_VGAP;
        constraints.anchor = GridBagConstraints.PAGE_END;
        content.add(setup.getMainComponent(), constraints);
    }

    @Override
    protected boolean isExtraSettingModified() {
        return setup.isModified((BazelProjectSettings) getInitialSettings());
    }

    @Override
    protected void resetExtraSettings(boolean isDefaultModuleCreation) {
        setup.setData((BazelProjectSettings) getInitialSettings());
    }

    @Override
    protected void applyExtraSettings(@NotNull BazelProjectSettings settings) {
        setup.getData(settings);
    }

    @Override
    public boolean validate(@NotNull BazelProjectSettings settings) throws ConfigurationException {
        return setup.validate();
    }
}
