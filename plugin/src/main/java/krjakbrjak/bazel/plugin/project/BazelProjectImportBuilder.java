package krjakbrjak.bazel.plugin.project;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.externalSystem.model.DataNode;
import com.intellij.openapi.externalSystem.model.project.ProjectData;
import com.intellij.openapi.externalSystem.service.project.ProjectDataManager;
import com.intellij.openapi.externalSystem.service.project.wizard.AbstractExternalProjectImportBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import icons.Icons;
import krjakbrjak.bazel.plugin.project.settings.ImportFromBazelControl;
import krjakbrjak.bazel.plugin.util.BazelConstants;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.util.ResourceBundle;

public class BazelProjectImportBuilder extends AbstractExternalProjectImportBuilder<ImportFromBazelControl> {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("Ui");

    public BazelProjectImportBuilder() {
        super(ProjectDataManager.getInstance(),
                ImportFromBazelControl::new,
                BazelConstants.SYSTEM_ID);
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) String getName() {
        return resourceBundle.getString("bazel.name");
    }

    @Override
    public Icon getIcon() {
        return Icons.BazelLogo16x16;
    }

    @Override
    protected void doPrepare(@NotNull WizardContext context) {
        String pathToUse = getFileToImport();
        VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByPath(pathToUse);
        if (file != null && !file.isDirectory() && file.getParent() != null) {
            pathToUse = file.getParent().getPath();
        }

        final ImportFromBazelControl importFromBazelControl = getControl(context.getProject());
        importFromBazelControl.setLinkedProjectPath(pathToUse);
    }

    @Override
    protected void beforeCommit(@NotNull DataNode<ProjectData> dataNode, @NotNull Project project) {
    }

    @Override
    protected @NotNull File getExternalProjectConfigToUse(@NotNull File file) {
        return file.isDirectory() ? file : file.getParentFile();
    }

    @Override
    protected void applyExtraSettings(@NotNull WizardContext context) {
    }
}
