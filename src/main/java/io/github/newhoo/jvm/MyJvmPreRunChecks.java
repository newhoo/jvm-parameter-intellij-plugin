package io.github.newhoo.jvm;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.runners.JavaProgramPatcher;
import io.github.newhoo.jvm.setting.PluginProjectSetting;
import org.apache.commons.lang3.StringUtils;

/**
 * MyJvmPreRunChecks
 *
 * @author huzunrong
 * @since 1.0
 */
public class MyJvmPreRunChecks extends JavaProgramPatcher {

    @Override
    public void patchJavaParameters(Executor executor, RunProfile configuration, JavaParameters javaParameters) {
        if (configuration instanceof RunConfiguration) {
            RunConfiguration runConfiguration = (RunConfiguration) configuration;

            // 增加自定义jvm参数
            String jvmParameter = new PluginProjectSetting(runConfiguration.getProject()).getJvmParameter();
            if (StringUtils.isNotBlank(jvmParameter)) {
                javaParameters.getVMParametersList().addParametersString(jvmParameter);
            }
        }
    }
}