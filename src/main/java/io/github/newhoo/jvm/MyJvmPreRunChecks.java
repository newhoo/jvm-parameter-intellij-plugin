package io.github.newhoo.jvm;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.runners.JavaProgramPatcher;
import io.github.newhoo.jvm.setting.GlobalJvmParameterSetting;
import io.github.newhoo.jvm.setting.JvmParameter;
import io.github.newhoo.jvm.setting.JvmParameterSetting;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

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
            List<JvmParameter> globalJvmParameterList = GlobalJvmParameterSetting.getInstance().getJvmParameterList();
            List<JvmParameter> jvmParameterSetting = JvmParameterSetting.getInstance(runConfiguration.getProject()).getJvmParameterList();
            globalJvmParameterList.addAll(jvmParameterSetting);

            String runParameter = globalJvmParameterList.stream()
                                                        .map(JvmParameter::toRunParameter)
                                                        .filter(StringUtils::isNotEmpty)
                                                        .collect(Collectors.joining(" "));
            if (StringUtils.isNotBlank(runParameter)) {
                javaParameters.getVMParametersList().addParametersString(runParameter);
            }
        }
    }
}