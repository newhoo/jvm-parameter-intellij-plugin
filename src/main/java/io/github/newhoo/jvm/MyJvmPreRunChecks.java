package io.github.newhoo.jvm;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.runners.JavaProgramPatcher;
import io.github.newhoo.jvm.setting.GlobalSetting;
import io.github.newhoo.jvm.setting.JvmParameter;
import io.github.newhoo.jvm.setting.ProjectSetting;
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
            List<JvmParameter> globalJvmParameterList = GlobalSetting.getInstance().getState().getJvmParameterList();
            List<JvmParameter> jvmParameterSetting = ProjectSetting.getInstance(runConfiguration.getProject()).getState().getJvmParameterList();

            String runParameter1 = globalJvmParameterList.stream()
                                                         .map(JvmParameter::toRunParameter)
                                                         .filter(StringUtils::isNotEmpty)
                                                         .collect(Collectors.joining(" "));
            String runParameter2 = jvmParameterSetting.stream()
                                                      .map(JvmParameter::toRunParameter)
                                                      .filter(StringUtils::isNotEmpty)
                                                      .collect(Collectors.joining(" "));
            if (StringUtils.isNotBlank(runParameter1)) {
                javaParameters.getVMParametersList().addParametersString(runParameter1);
            }
            if (StringUtils.isNotBlank(runParameter2)) {
                javaParameters.getVMParametersList().addParametersString(runParameter2);
            }
        }
    }
}