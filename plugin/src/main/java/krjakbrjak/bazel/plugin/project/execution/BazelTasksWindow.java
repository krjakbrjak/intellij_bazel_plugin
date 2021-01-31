package krjakbrjak.bazel.plugin.project.execution;

import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.Tree;
import krjakbrjak.bazel.tasks.BazelTask;
import krjakbrjak.bazel.tasks.CleanTask;
import krjakbrjak.bazel.tasks.DebugTask;
import krjakbrjak.bazel.tasks.RunTask;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ResourceBundle;

public class BazelTasksWindow {
    private static final int CLICKS_TO_TRIGGER_TARGET = 2;
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("Ui");
    private final Project project;
    private Tree treeView;
    private JPanel mainPanel;

    public BazelTasksWindow(Project project) {
        this.project = project;
    }

    public JComponent getMainComponent() {
        return mainPanel;
    }

    private void createUIComponents() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(resourceBundle.getString("bazel.toolwindow.tasks.node.name"));
        top.add(new DefaultMutableTreeNode(new RunTask()));
        top.add(new DefaultMutableTreeNode(new DebugTask()));
        top.add(new DefaultMutableTreeNode(new CleanTask()));
        treeView = new Tree(top);
        treeView.setCellRenderer(new TreeCellRenderer());
        ToolTipManager.sharedInstance().registerComponent(treeView);
        treeView.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == CLICKS_TO_TRIGGER_TARGET) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                            treeView.getLastSelectedPathComponent();
                    if (node != null && node.isLeaf()) {
                        BazelTask target = (BazelTask) node.getUserObject();
                        RunnerAndConfigurationSettings settings = RunManager.getInstance(project)
                                .createConfiguration(target.getDisplayName(), BazelConfigurationType.class);
                        BazelRunConfiguration runConfiguration = (BazelRunConfiguration) settings.getConfiguration();
                        var taskSettings = runConfiguration.getSettings();
                        taskSettings.setExternalProjectPath(project.getBasePath());
                        taskSettings.setTaskNames(List.of(target.getName()));
                        taskSettings.setScriptParameters(String.join(" ", target.getOptions()));
                        RunManager.getInstance(project).makeStable(settings);
                        RunManager.getInstance(project).addConfiguration(settings);
                        RunManager.getInstance(project).refreshUsagesList(runConfiguration);
                        ProgramRunnerUtil.executeConfiguration(settings,
                                DefaultRunExecutor.getRunExecutorInstance());
                    }
                }
            }
        });
    }
}
