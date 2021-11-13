package io.github.newhoo.jvm.setting;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
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
@State(name = "GlobalJvmParameterSetting", storages = {@Storage("global-jvm-parameter-settings.xml")})
public class GlobalJvmParameterSetting implements PersistentStateComponent<GlobalJvmParameterSetting> {

    public static GlobalJvmParameterSetting getInstance() {
        return ServiceManager.getService(GlobalJvmParameterSetting.class);
    }

    private List<JvmParameter> jvmParameterList = new ArrayList<>();

    public List<JvmParameter> getJvmParameterList() {
        return jvmParameterList;
    }

    public void setJvmParameterList(List<JvmParameter> jvmParameterList) {
        this.jvmParameterList = jvmParameterList;
    }

    @NotNull
    @Override
    public GlobalJvmParameterSetting getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull GlobalJvmParameterSetting state) {
        XmlSerializerUtil.copyBean(state, Objects.requireNonNull(getState()));
    }

    public boolean isModified(GlobalJvmParameterSetting globalJvmParameterSetting) {
        return this.jvmParameterList.size() != globalJvmParameterSetting.jvmParameterList.size()
                || !toCompareString(this.jvmParameterList).equals(toCompareString(globalJvmParameterSetting.jvmParameterList));
    }

    private String toCompareString(List<JvmParameter> jvmParameterList) {
        return jvmParameterList.stream().map(JvmParameter::toCompareString).collect(Collectors.joining("@"));
    }
}