package io.github.newhoo.jvm.setting;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * SettingConfigurable
 *
 * @author huzunrong
 * @since 1.0
 */
public class SettingConfigurable implements Configurable {

    private final JvmParameterSetting jvmParameterSetting;
    private final GlobalJvmParameterSetting globalJvmParameterSetting;
    private final SettingForm settingForm;

    public SettingConfigurable(Project project) {
        this.jvmParameterSetting = JvmParameterSetting.getInstance(project);
        this.globalJvmParameterSetting = GlobalJvmParameterSetting.getInstance();
        this.settingForm = new SettingForm(project);
    }

    @Nls(capitalization = Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Jvm Parameter";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return settingForm.mainPanel;
    }

    @Override
    public boolean isModified() {
        Pair<JvmParameterSetting, GlobalJvmParameterSetting> modifiedSetting = settingForm.getModifiedSetting();
        return jvmParameterSetting.isModified(modifiedSetting.getLeft())
                || globalJvmParameterSetting.isModified(modifiedSetting.getRight());
    }

    @Override
    public void apply() {
        settingForm.saveTo(jvmParameterSetting, globalJvmParameterSetting);
    }

    @Override
    public void reset() {
        settingForm.reset(jvmParameterSetting, globalJvmParameterSetting);
    }
}