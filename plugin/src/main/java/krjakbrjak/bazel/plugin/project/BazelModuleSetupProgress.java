package krjakbrjak.bazel.plugin.project;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.AnimatedIcon;

import javax.swing.*;

public class BazelModuleSetupProgress {
    private JTextArea commandLogsArea;
    private JPanel mainPanel;
    private JLabel errorLabel;

    public void append(String line) {
        commandLogsArea.append(line);
    }

    public void setError(boolean isError) {
        errorLabel.setVisible(isError);
    }

    private void createUIComponents() {
        commandLogsArea = new JTextArea();
        commandLogsArea.setEditable(false);
        commandLogsArea.setBorder(null);
        commandLogsArea.setLineWrap(true);

        errorLabel = new JLabel();
        Icon icon = AllIcons.General.Error;
        errorLabel.setIcon(new AnimatedIcon(1000, icon, IconLoader.getDisabledIcon(icon)));
        errorLabel.setVisible(false);
    }

    public JComponent getMainComponent() {
        return mainPanel;
    }
}
