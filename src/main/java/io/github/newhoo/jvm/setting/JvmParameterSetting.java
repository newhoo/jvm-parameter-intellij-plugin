package io.github.newhoo.jvm.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JvmParameterSetting
 *
 * @author huzunrong
 * @since 1.0.3
 */
public class JvmParameterSetting {

    /** 必须包含Getter/Setter */
    private List<JvmParameter> jvmParameterList = new ArrayList<>();

    public List<JvmParameter> getJvmParameterList() {
        return jvmParameterList;
    }

    public void setJvmParameterList(List<JvmParameter> jvmParameterList) {
        this.jvmParameterList = jvmParameterList;
    }

    public boolean isModified(JvmParameterSetting jvmParameterSetting) {
        return this.jvmParameterList.size() != jvmParameterSetting.jvmParameterList.size()
                || !toCompareString(this.jvmParameterList).equals(toCompareString(jvmParameterSetting.jvmParameterList));
    }

    private String toCompareString(List<JvmParameter> jvmParameterList) {
        return jvmParameterList.stream().map(JvmParameter::toCompareString).collect(Collectors.joining("@"));
    }
}