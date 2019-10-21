package io.github.newhoo.jvm.setting;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

/**
 * PluginProjectSetting
 *
 * @author huzunrong
 * @since 1.0
 */
public class PluginProjectSetting {

    private static final String KEY_JMV_PARAMETER = "jvm-parameter.jvmParameter";
    private static final String KEY_JMV_PARAMETER_LIST = "jvm-parameter.jvmParameterList";

    private final PropertiesComponent propertiesComponent;

    public PluginProjectSetting(Project project) {
        this.propertiesComponent = PropertiesComponent.getInstance(project);
    }

    public String getJvmParameter() {
        return propertiesComponent.getValue(KEY_JMV_PARAMETER);
    }

    public void setJvmParameter(String jvmParameter) {
        propertiesComponent.setValue(KEY_JMV_PARAMETER, jvmParameter);
    }

    public String getJvmParameterList() {
        return propertiesComponent.getValue(KEY_JMV_PARAMETER_LIST);
    }

    public void setJvmParameterList(String jvmParameterList) {
        propertiesComponent.setValue(KEY_JMV_PARAMETER_LIST, jvmParameterList);
    }
}