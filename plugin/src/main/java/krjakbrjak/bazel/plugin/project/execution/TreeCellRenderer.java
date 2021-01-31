package krjakbrjak.bazel.plugin.project.execution;

import com.intellij.ide.util.treeView.NodeRenderer;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.util.ui.tree.TreeUtil;
import krjakbrjak.bazel.tasks.BazelTask;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TreeCellRenderer extends NodeRenderer {
    public TreeCellRenderer() {
        super();
    }

    @Override
    public void customizeCellRenderer(@NotNull JTree tree, @NlsSafe Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.customizeCellRenderer(tree, value, selected, expanded, leaf, row, hasFocus);
        Object obj = TreeUtil.getUserObject(value);
        if (obj instanceof BazelTask) {
            BazelTask task = (BazelTask) obj;
            clear();
            append(task.getDisplayName());
            setToolTipText(task.getDescription());
        }
    }
}
