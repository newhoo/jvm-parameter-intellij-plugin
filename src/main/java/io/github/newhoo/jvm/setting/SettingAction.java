package io.github.newhoo.jvm.setting;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author huzunrong
 * @date 2021/11/26 5:31 下午
 * @since 1.0.4
 */
public class SettingAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        if (e.getProject() != null) {
            ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), SettingConfigurable.class);
        }
    }
}
