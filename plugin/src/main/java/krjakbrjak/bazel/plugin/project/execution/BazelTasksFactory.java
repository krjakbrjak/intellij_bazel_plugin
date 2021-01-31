package krjakbrjak.bazel.plugin.project.execution;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import krjakbrjak.bazel.plugin.settings.BazelSettings;
import org.jetbrains.annotations.NotNull;

public class BazelTasksFactory implements ToolWindowFactory {
    @Override
    public boolean isApplicable(@NotNull Project project) {
        return project.getService(BazelSettings.class) != null;
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(new BazelTasksWindow(project).getMainComponent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
