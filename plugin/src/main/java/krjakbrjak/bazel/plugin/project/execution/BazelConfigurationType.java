package krjakbrjak.bazel.plugin.project.execution;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import icons.Icons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class BazelConfigurationType implements ConfigurationType {
    public static BazelConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(BazelConfigurationType.class);
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "Bazel";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) String getConfigurationTypeDescription() {
        return "Delegates execution to Bazel";
    }

    @Override
    public Icon getIcon() {
        return Icons.BazelLogo16x16;
    }

    @Override
    public @NotNull
    @NonNls
    String getId() {
        return "Bazel";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{
                new ConfigurationFactory(this) {
                    @Override
                    public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
                        return new BazelRunConfiguration(project, this, BazelConfigurationType.this.getId());
                    }
                }
        };
    }
}
