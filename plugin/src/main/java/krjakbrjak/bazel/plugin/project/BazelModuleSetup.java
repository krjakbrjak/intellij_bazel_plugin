package krjakbrjak.bazel.plugin.project;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import krjakbrjak.bazel.plugin.settings.BazelProjectSettings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class BazelModuleSetup {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("Ui");
    // Holds the list of targets. That basically duplicates the content of
    // targetModel, but having it here makes it easier to decide if combobox
    // model needs to be updated with new data or not. Otherwise, extraction
    // of items from the model is required plus its further sorting in order
    // to make a comparison.
    private List<String> currentTargets = List.of();
    private List<String> currentPackages = List.of();
    private List<String> currentExecutable = List.of();
    private JPanel mainPanel;
    private JPanel optionsPanel;
    private DefaultComboBoxModel<String> targetModel;
    private ComboBox<String> targetComboBox;
    private TextFieldWithBrowseButton absolutePath;
    private DefaultComboBoxModel<String> packageModel;
    private ComboBox<String> packageComboBox;
    private DefaultTableModel model;
    private JBTable optionsList;

    private BazelModuleSetup() {
    }

    public static BazelModuleSetup getInstance() {
        return new BazelModuleSetup();
    }

    public JComponent getMainComponent() {
        return mainPanel;
    }

    public void subscribeForChanges(BazelModuleSetupListener listener) {
        if (model != null) {
            model.addTableModelListener(e -> {
                BazelProjectSettings settings = new BazelProjectSettings();
                settings.setExecutable(getExecutable());
                setData(settings);
                listener.onBazelExecutableChanged(getExecutable());
            });
        }

        if (targetComboBox != null) {
            targetComboBox.addItemListener(item -> {
                if (item.getStateChange() == ItemEvent.SELECTED) {
                    listener.onTargetChanged((String) item.getItem());
                }
            });
        }

        if (packageComboBox != null) {
            packageComboBox.addItemListener(item -> {
                if (item.getStateChange() == ItemEvent.SELECTED) {
                    listener.onPackageChanged((String) item.getItem());
                }
            });
        }
    }

    private void addOption() {
        model.addRow(new String[]{""});
    }

    private void removeAction() {
        model.removeRow(optionsList.getSelectedRow());
    }

    private void createUIComponents() {
        model = new DefaultTableModel(0, 1);
        model.setColumnIdentifiers(new String[]{resourceBundle.getString("bazel.setup.options")});
        optionsList = new JBTable(model);
        optionsPanel = ToolbarDecorator.createDecorator(optionsList)
                .setAddAction(anActionButton -> addOption())
                .setRemoveAction(anActionButton -> removeAction())
                .createPanel();

        absolutePath = new TextFieldWithBrowseButton();
        absolutePath.addBrowseFolderListener(new TextBrowseFolderListener(
                new FileChooserDescriptor(false,
                        true,
                        true,
                        true,
                        true,
                        true
                )
        ));

        packageModel = new DefaultComboBoxModel<>();
        packageComboBox = new ComboBox<>(packageModel);

        targetModel = new DefaultComboBoxModel<>();
        targetComboBox = new ComboBox<>(targetModel);
    }

    /**
     * Updates form.
     *
     * @param data {@link krjakbrjak.bazel.plugin.settings.BazelProjectSettings} object.
     */
    public void setData(BazelProjectSettings data) {
        List<String> exec = data.getExecutable();
        if (!Objects.equals(exec, currentExecutable)) {
            currentExecutable = exec;
            if (exec != null) {
                Object[][] tmp = new Object[exec.size()][1];
                for (int i = 0; i < exec.size(); ++i) {
                    tmp[i][0] = exec.get(i);
                }
                this.model.setDataVector(tmp, new Object[]{resourceBundle.getString("bazel.setup.options")});
            }
        }

        List<String> packages = data.getPackages();
        if (!Objects.equals(packages, currentPackages)) {
            currentPackages = packages;
            packageModel.removeAllElements();

            if (packages != null) {
                packageModel.addAll(packages);
                packageComboBox.setEnabled(true);
            } else {
                packageComboBox.setEnabled(false);
            }
        }

        if (packages != null) {
            int current = data.getCurrentPackage();
            if (current > -1 && current < packages.size() && current != packageComboBox.getSelectedIndex()) {
                packageComboBox.setSelectedIndex(current);
            }
        }

        List<String> targets = data.getTargets();
        if (!Objects.equals(targets, currentTargets)) {
            currentTargets = targets;
            targetModel.removeAllElements();

            if (targets != null) {
                targetModel.addAll(targets);
                targetComboBox.setEnabled(true);
            } else {
                targetComboBox.setEnabled(false);
            }
        }

        if (targets != null) {
            int current = data.getCurrentTarget();
            if (current > -1 && current < targets.size() && current != targetComboBox.getSelectedIndex()) {
                targetComboBox.setSelectedIndex(current);
            }
        }
    }

    private List<String> getExecutable() {
        if (model.getRowCount() > 0) {
            return model.getDataVector()
                    .stream()
                    .map(vector -> (String) vector.elementAt(0)).collect(Collectors.toList());
        }
        return null;
    }

    private List<String> getTargets() {
        if (targetModel.getSize() > 0) {
            ArrayList<String> targets = new ArrayList<>();
            for (int i = 0; i < targetModel.getSize(); ++i) {
                targets.add(targetModel.getElementAt(i));
            }

            return targets;
        }
        return null;
    }

    private List<String> getPackages() {
        if (packageModel.getSize() > 0) {
            ArrayList<String> targets = new ArrayList<>();
            for (int i = 0; i < packageModel.getSize(); ++i) {
                targets.add(packageModel.getElementAt(i));
            }

            return targets;
        }
        return null;
    }

    private int getCurrentTarget() {
        Object selectedTarget = targetModel.getSelectedItem();
        if (selectedTarget == null) {
            return -1;
        }

        return targetModel.getIndexOf(selectedTarget);
    }

    private int getCurrentPackage() {
        Object selectedTarget = packageModel.getSelectedItem();
        if (selectedTarget == null) {
            return -1;
        }

        return packageModel.getIndexOf(selectedTarget);
    }

    /**
     * Updates project settings with the form's data.
     *
     * @param data {@link krjakbrjak.bazel.plugin.settings.BazelProjectSettings} object.
     */
    public void getData(BazelProjectSettings data) {
        data.setExecutable(getExecutable());
        data.setPackages(getPackages());
        data.setCurrentPackage(getCurrentPackage());
        data.setTargets(getTargets());
        data.setCurrentTarget(getCurrentTarget());
    }

    public boolean isModified(BazelProjectSettings data) {
        return !Objects.equals(getExecutable(), data.getExecutable()) &&
                !Objects.equals(getPackages(), data.getPackages()) &&
                !Objects.equals(getCurrentPackage(), data.getCurrentPackage()) &&
                !Objects.equals(getTargets(), data.getTargets()) &&
                !Objects.equals(getCurrentTarget(), data.getCurrentTarget());
    }

    /**
     * Validates setup form. The imported project is considered to be valid
     * if there are bazel targets available.
     * TODO: add support for bazel build files, this way validation isn't gonna be that strict.
     *
     * @return {@code boolean}
     */
    public boolean validate() {
        return getExecutable() != null && getExecutable().size() > 0 &&
                targetModel.getSize() > 0 &&
                targetModel.getSelectedItem() != null;
    }
}
