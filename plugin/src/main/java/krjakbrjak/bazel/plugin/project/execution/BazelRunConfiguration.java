package krjakbrjak.bazel.plugin.project.execution;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.externalSystem.service.execution.ExternalSystemRunConfiguration;
import com.intellij.openapi.project.Project;
import krjakbrjak.bazel.plugin.util.BazelConstants;

public class BazelRunConfiguration extends ExternalSystemRunConfiguration {
    public BazelRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(BazelConstants.SYSTEM_ID, project, factory, name);
    }
}
