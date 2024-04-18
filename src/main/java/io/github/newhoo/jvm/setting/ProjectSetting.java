package io.github.newhoo.jvm.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * ProjectSetting
 *
 * @author huzunrong
 * @since 1.0.3
 */
@State(name = "JvmParameterSetting", storages = {@Storage("jvm-parameter-settings.xml")})
public class ProjectSetting implements PersistentStateComponent<JvmParameterSetting> {

    public static ProjectSetting getInstance(Project project) {
        return project.getService(ProjectSetting.class);
//        return ServiceManager.getService(project, ProjectSetting.class);
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