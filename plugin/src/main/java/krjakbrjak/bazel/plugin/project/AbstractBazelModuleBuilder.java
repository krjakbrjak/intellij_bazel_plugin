package krjakbrjak.bazel.plugin.project;

import com.intellij.openapi.externalSystem.service.project.wizard.AbstractExternalModuleBuilder;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.InvalidDataException;
import krjakbrjak.bazel.plugin.settings.BazelProjectSettings;
import krjakbrjak.bazel.plugin.util.BazelConstants;
import krjakbrjak.bazel.plugin.util.Icons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AbstractBazelModuleBuilder extends AbstractExternalModuleBuilder<BazelProjectSettings> {
    public AbstractBazelModuleBuilder() {
        super(BazelConstants.SYSTEM_ID, new BazelProjectSettings());
    }

    @Override
    public Icon getNodeIcon() {
        return Icons.BazelLogo;
    }

    @Override
    public @NotNull Module createModule(@NotNull ModifiableModuleModel moduleModel) throws InvalidDataException, ConfigurationException {
        final String moduleFilePath = getModuleFilePath();
        deleteModuleFile(moduleFilePath);
        String moduleTypeId = getModuleType().getId();
        Module module = moduleModel.newModule(moduleFilePath, moduleTypeId);
        setupModule(module);
        return module;
    }

    @Override
    public ModuleType<?> getModuleType() {
        return StdModuleTypes.JAVA;
    }
}
