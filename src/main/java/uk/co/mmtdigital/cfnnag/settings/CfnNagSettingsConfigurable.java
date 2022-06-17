package uk.co.mmtdigital.cfnnag.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CfnNagSettingsConfigurable implements Configurable {

    private CfnNagSettingsComponent settingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "CfnNag";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return settingsComponent.getPreferredFocusedComponent();
    }

    @Override
    public @Nullable JComponent createComponent() {
        settingsComponent = new CfnNagSettingsComponent();
        return settingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        CfnNagSettingsState settings = CfnNagSettingsState.getInstance();
        boolean modified  = !settingsComponent.getExecutablePathText().equals(settings.executablePath);
                modified |= !settingsComponent.getBlacklistPathText().equals(settings.blacklistPath);
                modified |= !settingsComponent.getRuleDirectoryText().equals(settings.ruleDirectory);
        return modified;
    }

    @Override
    public void apply() {
        CfnNagSettingsState settings = CfnNagSettingsState.getInstance();
        settings.executablePath = settingsComponent.getExecutablePathText();
        settings.blacklistPath = settingsComponent.getBlacklistPathText();
        settings.ruleDirectory = settingsComponent.getRuleDirectoryText();
    }

    @Override
    public void reset() {
        CfnNagSettingsState settings = CfnNagSettingsState.getInstance();
        settingsComponent.setExecutablePathText(settings.executablePath);
        settingsComponent.setBlacklistPathText(settings.blacklistPath);
        settingsComponent.setRuleDirectoryText(settings.ruleDirectory);
    }

    @Override
    public void disposeUIResources() {
        settingsComponent = null;
    }
}
