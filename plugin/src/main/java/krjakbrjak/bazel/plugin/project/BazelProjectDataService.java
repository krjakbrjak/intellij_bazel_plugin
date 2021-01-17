package krjakbrjak.bazel.plugin.project;

import com.intellij.openapi.externalSystem.model.DataNode;
import com.intellij.openapi.externalSystem.model.Key;
import com.intellij.openapi.externalSystem.model.project.ProjectData;
import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider;
import com.intellij.openapi.externalSystem.service.project.manage.AbstractProjectDataService;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * TODO: extend project with yet another module that handles bazel build/workspace files.
 */
public class BazelProjectDataService extends AbstractProjectDataService<BazelClasspathData, Module> {
    @Override
    public @NotNull Key<BazelClasspathData> getTargetDataKey() {
        return BazelClasspathData.KEY;
    }

    @Override
    public void importData(@NotNull Collection<DataNode<BazelClasspathData>> toImport, @Nullable ProjectData projectData, @NotNull Project project, @NotNull IdeModifiableModelsProvider modelsProvider) {
        super.importData(toImport, projectData, project, modelsProvider);
    }
}
