package krjakbrjak.bazel.plugin.project;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.externalSystem.service.project.wizard.SelectExternalProjectStep;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import org.jetbrains.annotations.NotNull;

public class BazelModuleBuilder extends AbstractBazelModuleBuilder {
    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
        return new ModuleWizardStep[]{
                new SelectExternalProjectStep(wizardContext)
        };
    }
}
