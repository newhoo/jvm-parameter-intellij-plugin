package io.github.newhoo.jvm.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * JvmParameterSetting
 *
 * @author huzunrong
 * @since 1.0.3
 */
@State(name = "JvmParameterSetting", storages = {@Storage("jvm-parameter-settings.xml")})
public class JvmParameterSetting implements PersistentStateComponent<JvmParameterSetting> {

    public static JvmParameterSetting getInstance(Project project) {
        return ServiceManager.getService(project, JvmParameterSetting.class);
    }

    /** 必须包含Getter/Setter */
    private List<JvmParameter> jvmParameterList = new ArrayList<>();

    public List<JvmParameter> getJvmParameterList() {
        return jvmParameterList;
    }

    public void setJvmParameterList(List<JvmParameter> jvmParameterList) {
        this.jvmParameterList = jvmParameterList;
    }

    @NotNull
    @Override
    public JvmParameterSetting getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull JvmParameterSetting state) {
        XmlSerializerUtil.copyBean(state, Objects.requireNonNull(getState()));
    }

    public boolean isModified(JvmParameterSetting jvmParameterSetting) {
        return this.jvmParameterList.size() != jvmParameterSetting.jvmParameterList.size()
                || !toCompareString(this.jvmParameterList).equals(toCompareString(jvmParameterSetting.jvmParameterList));
    }

    private String toCompareString(List<JvmParameter> jvmParameterList) {
        return jvmParameterList.stream().map(JvmParameter::toCompareString).collect(Collectors.joining("@"));
    }
}