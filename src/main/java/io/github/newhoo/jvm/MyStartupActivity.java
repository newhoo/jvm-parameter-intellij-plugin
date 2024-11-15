package io.github.newhoo.jvm;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import io.github.newhoo.jvm.setting.JvmParameter;
import io.github.newhoo.jvm.setting.ProjectSetting;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目打开后的后台任务：合并旧版本的配置
 *
 * @since 1.0.3
 */
public class MyStartupActivity implements ProjectActivity {

    private static final String KEY_JMV_PARAMETER_LIST = "jvm-parameter.jvmParameterList";

    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(project);
        String jvmParameterTableText = propertiesComponent.getValue(KEY_JMV_PARAMETER_LIST);
        if (StringUtils.isEmpty(jvmParameterTableText)) {
            return null;
        }
        propertiesComponent.unsetValue(KEY_JMV_PARAMETER_LIST);

        List<JvmParameter> oldJvmParameterList = new ArrayList<>();
        String[] split = StringUtils.split(jvmParameterTableText, "@@@");
        if (split != null && split.length % 3 == 0) {
            for (int i = 0; i < split.length; i = i + 3) {
                JvmParameter jvmParameter = new JvmParameter();
                jvmParameter.setEnabled(BooleanUtils.toBoolean(split[i]));
                jvmParameter.setGlobal(false);
                jvmParameter.setName(StringUtils.trimToEmpty(split[i + 1]));
                jvmParameter.setValue(StringUtils.trimToEmpty(split[i + 2]));
                oldJvmParameterList.add(jvmParameter);
            }
        }
        if (!oldJvmParameterList.isEmpty()) {
            List<JvmParameter> jvmParameterList = ProjectSetting.getInstance(project).getState().getJvmParameterList();
            if (jvmParameterList.isEmpty()) {
                jvmParameterList.addAll(oldJvmParameterList);
            }
        }
        return null;
    }
}