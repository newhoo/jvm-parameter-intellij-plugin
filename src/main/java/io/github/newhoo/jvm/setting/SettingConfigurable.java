package io.github.newhoo.jvm.setting;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
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

    private final PluginProjectSetting projectSetting;
    private final SettingForm settingForm;

    public SettingConfigurable(Project project) {
        this.projectSetting = new PluginProjectSetting(project);
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
        reset();

        return settingForm.mainPanel;
    }

    @Override
    public boolean isModified() {
        return !StringUtils.equals(projectSetting.getJvmParameterList(), settingForm.getJvmParameterTableText());
    }

    @Override
    public void apply() {
        projectSetting.setJvmParameter(settingForm.jvmParameterText.getText());
        projectSetting.setJvmParameterList(settingForm.getJvmParameterTableText());
    }

    @Override
    public void reset() {
        settingForm.setJvmParameterTableText(projectSetting.getJvmParameterList());
    }
}