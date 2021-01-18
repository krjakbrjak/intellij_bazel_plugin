package krjakbrjak.bazel.plugin.project;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.externalSystem.service.project.wizard.AbstractExternalProjectImportProvider;
import com.intellij.openapi.externalSystem.service.project.wizard.SelectExternalProjectStep;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import krjakbrjak.bazel.plugin.util.BazelConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BazelProjectImportProvider extends AbstractExternalProjectImportProvider {
    public static final String WORKSPACE_FILENAME = "WORKSPACE";

    public BazelProjectImportProvider() {
        this(new BazelProjectImportBuilder());
    }

    public BazelProjectImportProvider(BazelProjectImportBuilder builder) {
        super(builder, BazelConstants.SYSTEM_ID);
    }

    /**
     * The directory containing a file 'WORKSPACE' or a 'WORKSPACE' file itself
     * can be both imported.
     *
     * @param fileOrDirectory A pth to a file or directory to import.
     * @param project         Project.
     * @return {@code boolean}
     */
    @Override
    public boolean canImport(@NotNull VirtualFile fileOrDirectory, @Nullable Project project) {
        if (fileOrDirectory.isDirectory() && fileOrDirectory.findFileByRelativePath(WORKSPACE_FILENAME) != null) {
            return true;
        } else {
            return canImportFromFile(fileOrDirectory);
        }
    }

    /**
     * Bazel project can be imported from a 'WORKSPACE' file.
     *
     * @param file A file to import.
     * @return {@code boolean}
     */
    @Override
    protected boolean canImportFromFile(VirtualFile file) {
        return file.getName().equals(WORKSPACE_FILENAME);
    }

    @Override
    public ModuleWizardStep[] createSteps(WizardContext context) {
        return new ModuleWizardStep[]{
                new SelectExternalProjectStep(context)
        };
    }
}
