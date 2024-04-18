package io.github.newhoo.jvm.setting;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * GlobalSetting
 *
 * @author huzunrong
 * @since 1.0.3
 */
@State(name = "GlobalJvmParameterSetting", storages = {@Storage("global-jvm-parameter-settings.xml")})
public class GlobalSetting implements PersistentStateComponent<JvmParameterSetting> {

    public static GlobalSetting getInstance() {
        return ApplicationManager.getApplication().getService(GlobalSetting.class);
//        return ServiceManager.getService(GlobalSetting.class);
    }

    private final JvmParameterSetting jvmParameterSetting = new JvmParameterSetting();

    @NotNull
    @Override
    public JvmParameterSetting getState() {
        return jvmParameterSetting;
    }

    @Override
    public void loadState(@NotNull JvmParameterSetting state) {
        XmlSerializerUtil.copyBean(state, Objects.requireNonNull(getState()));
    }

    public boolean isModified(JvmParameterSetting jvmParameterSetting) {
        return this.jvmParameterSetting.isModified(jvmParameterSetting);
    }
}