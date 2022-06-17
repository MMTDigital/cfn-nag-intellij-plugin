package uk.co.mmtdigital.cfnnag.settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CfnNagSettingsComponent {

    private final JPanel panel;
    private final JBTextField executablePath = new JBTextField();
    private final JBTextField blacklistPath = new JBTextField();
    private final JBTextField ruleDirectory = new JBTextField();

    public CfnNagSettingsComponent() {
        panel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Executable path: "), executablePath, 1, false)
                .addLabeledComponent(new JBLabel("Blacklist path: "), blacklistPath, 1, false)
                .addLabeledComponent(new JBLabel("Rule directory: "), ruleDirectory, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return panel;
    }

    @NotNull
    public JComponent getPreferredFocusedComponent() {
        return executablePath;
    }

    @NotNull
    public String getExecutablePathText() {
        return executablePath.getText();
    }

    @NotNull
    public String getBlacklistPathText() {
        return blacklistPath.getText();
    }

    @NotNull
    public String getRuleDirectoryText() {
        return ruleDirectory.getText();
    }

    public void setExecutablePathText(@NotNull String executablePathText){
        executablePath.setText(executablePathText);
    }

    public void setBlacklistPathText(@NotNull String blacklistPathText){
        blacklistPath.setText(blacklistPathText);
    }

    public void setRuleDirectoryText(@NotNull String ruleDirectoryText){
        ruleDirectory.setText(ruleDirectoryText);
    }

}
