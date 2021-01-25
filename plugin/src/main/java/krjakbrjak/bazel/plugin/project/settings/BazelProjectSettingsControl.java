package krjakbrjak.bazel.plugin.project.settings;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.externalSystem.service.settings.AbstractExternalProjectSettingsControl;
import com.intellij.openapi.externalSystem.util.ExternalSystemUiUtil;
import com.intellij.openapi.externalSystem.util.PaintAwarePanel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.concurrency.EdtExecutorService;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.UIUtil;
import krjakbrjak.bazel.*;
import krjakbrjak.bazel.plugin.project.BazelModuleSetup;
import krjakbrjak.bazel.plugin.project.BazelModuleSetupListener;
import krjakbrjak.bazel.plugin.project.ProgressDialog;
import krjakbrjak.bazel.plugin.settings.BazelProjectSettings;
import krjakbrjak.bazel.plugin.settings.BazelSettings;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BazelProjectSettingsControl extends AbstractExternalProjectSettingsControl<BazelProjectSettings> {
    private final BazelModuleSetup setup = BazelModuleSetup.getInstance();

    public BazelProjectSettingsControl(@NotNull BazelProjectSettings initialSettings) {
        super(initialSettings);
        setup.subscribeForChanges(new BazelModuleSetupListener() {
            @Override
            public void onBazelExecutableChanged(List<String> executable) {
                BazelProjectSettings settings = getInitialSettings();
                // Sets an executable and invalidates the rest of the settings
                settings.setExecutable(executable);
                settings.setPackages(null);
                settings.setCurrentPackage(-1);
                settings.setTargets(null);
                settings.setCurrentTarget(-1);
                ProgressDialog dialog = new ProgressDialog();
                Handle<List<String>> handle = null;
                try {
                    handle = getPackages(settings, (line, isError) -> dialog.append(line + "\n"));
                    handle.onExit()
                            .thenApplyAsync(packages -> {
                                settings.setPackages(packages);
                                if (packages == null) {
                                    return Optional.of(false);
                                }
                                return Optional.of(true);
                            })
                            .thenApplyAsync(optionalBoolean -> {
                                optionalBoolean.ifPresent(ok -> {
                                    dialog.setError(ok);
                                    if (ok) {
                                        EdtExecutorService.getInstance().execute(() ->
                                                dialog.close(DialogWrapper.OK_EXIT_CODE));
                                    }
                                });
                                return null;
                            });
                } catch (IOException e) {
                    dialog.append(e.getLocalizedMessage() + "\n");
                    dialog.setError(true);
                }
                if (!dialog.showAndGet()) {
                    settings.setExecutable(null);
                }
                if (handle != null) {
                    handle.cancel(false);
                }
                setup.setData(settings, true);
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
                        EdtExecutorService.getInstance().execute(() -> {
                            ProgressDialog dialog = new ProgressDialog();
                            Handle<List<String>> handle = null;
                            try {
                                handle = getTargets(settings, (line, isError) -> dialog.append(line + "\n"));
                                handle.onExit()
                                        .thenApplyAsync(targets -> {
                                            settings.setTargets(targets);
                                            if (targets == null) {
                                                return Optional.of(false);
                                            }
                                            return Optional.of(true);
                                        })
                                        .thenApplyAsync(optionalBoolean -> {
                                            optionalBoolean.ifPresent(ok -> {
                                                dialog.setError(ok);
                                                if (ok) {
                                                    EdtExecutorService.getInstance().execute(() ->
                                                            dialog.close(DialogWrapper.OK_EXIT_CODE));
                                                }
                                            });
                                            return null;
                                        });
                            } catch (IOException e) {
                                dialog.setError(true);
                                dialog.append(e.getLocalizedMessage() + "\n");
                            }
                            if (!dialog.showAndGet()) {
                                settings.setPackages(null);
                                //settings.setExecutable(null);
                            }
                            if (handle != null) {
                                handle.cancel(false);
                            }
                            setup.setData(settings, true);
                        });
                    }
                }
            }
        });
    }

    private static Handle<List<String>> getTargets(@NotNull BazelProjectSettings settings, CommandLogger logger) throws IOException {
        ExecutableContext exeCtx = ServiceManager.getService(Library.class)
                .getContext()
                .getExecutableBuilder()
                .withCommand(settings.getExecutable())
                .build();
        return ServiceManager.getService(BazelCommands.class).queryAllTargets(
                exeCtx,
                settings.getExternalProjectPath(),
                settings.getPackages().get(settings.getCurrentPackage()),
                logger);
    }

    private static Handle<List<String>> getPackages(@NotNull BazelProjectSettings settings, CommandLogger logger) throws IOException {
        ExecutableContext exeCtx = ServiceManager.getService(Library.class)
                .getContext()
                .getExecutableBuilder()
                .withCommand(settings.getExecutable())
                .build();
        return ServiceManager.getService(BazelCommands.class).queryAllPackages(
                exeCtx,
                settings.getExternalProjectPath(), logger);
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
        return setup.isModified(getInitialSettings());
    }

    @Override
    protected void resetExtraSettings(boolean isDefaultModuleCreation) {
        setup.setData(getInitialSettings(), true);
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
