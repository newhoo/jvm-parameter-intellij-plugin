package io.github.newhoo.jvm.setting;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author huzunrong
 * @date 2021/11/12 9:03 上午
 * @since 1.0.3
 */
public class JvmParameter {

    private Boolean enabled;
    private String name;
    private String value;
    private Boolean isGlobal;

    public JvmParameter() {
    }

    public JvmParameter(Boolean enabled, String name, String value, Boolean isGlobal) {
        this.enabled = enabled;
        this.name = name;
        this.value = value;
        this.isGlobal = isGlobal;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getGlobal() {
        return isGlobal;
    }

    public void setGlobal(Boolean global) {
        isGlobal = global;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toRunParameter() {
        if (BooleanUtils.isTrue(enabled) && StringUtils.isNotEmpty(String.valueOf(name))) {
            if (StringUtils.isEmpty(value)) {
                return String.valueOf(name);
            }
            return !StringUtils.containsWhitespace(value)
                    ? "-D" + name + "=" + value
                    : "\"-D" + name + "=" + value + "\"";
        }
        return "";
    }

    public String toRunParameter2() {
        if (BooleanUtils.isTrue(enabled) && StringUtils.isNotEmpty(String.valueOf(name))) {
            if (StringUtils.isEmpty(value)) {
                return "\"" + name + "\"";
            }
            return "\"-D" + name + "=" + value + "\"";
        }
        return "";
    }

    public String toCompareString() {
        return enabled + StringUtils.defaultString(name) + StringUtils.defaultString(value) + isGlobal;
    }
}
