package uk.co.mmtdigital.cfnnag.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "uk.me.pilgrim.cfnnag.settings.CfnNagSettingsState",
        storages = @Storage("CfnNagPlugin.xml")
)
public class CfnNagSettingsState implements PersistentStateComponent<CfnNagSettingsState> {

    public String executablePath = "cfn_nag";
    public String blacklistPath = "";
    public String ruleDirectory = "";

    public static CfnNagSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(CfnNagSettingsState.class);
    }

    @Override
    public @Nullable CfnNagSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CfnNagSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
