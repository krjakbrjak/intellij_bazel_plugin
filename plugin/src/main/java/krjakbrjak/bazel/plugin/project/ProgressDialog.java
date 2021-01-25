package krjakbrjak.bazel.plugin.project;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ProgressDialog extends DialogWrapper {
    private final BazelModuleSetupProgress progress = new BazelModuleSetupProgress();

    public ProgressDialog() {
        super(false);
        init();
        setTitle("Progress");
    }

    public void setError(boolean isError) {
        progress.setError(isError);
    }

    public void append(String line) {
        progress.append(line);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return progress.getMainComponent();
    }

    @Override
    protected Action @NotNull [] createActions() {
        return new Action[]{};
    }
}
