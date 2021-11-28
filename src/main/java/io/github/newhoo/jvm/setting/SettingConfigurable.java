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

    private final ProjectSetting projectSetting;
    private final GlobalSetting globalSetting;
    private final SettingForm settingForm;

    public SettingConfigurable(Project project) {
        this.projectSetting = ProjectSetting.getInstance(project);
        this.globalSetting = GlobalSetting.getInstance();
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
        Pair<JvmParameterSetting, JvmParameterSetting> modifiedSetting = settingForm.getModifiedSetting();
        return globalSetting.isModified(modifiedSetting.getLeft())
                || projectSetting.isModified(modifiedSetting.getRight());
    }

    @Override
    public void apply() {
        settingForm.saveTo(globalSetting.getState(), projectSetting.getState());
    }

    @Override
    public void reset() {
        settingForm.reset(globalSetting.getState(), projectSetting.getState());
    }
}