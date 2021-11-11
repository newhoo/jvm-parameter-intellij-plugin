package io.github.newhoo.jvm.setting;

import com.intellij.openapi.project.Project;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.TableUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import io.github.newhoo.jvm.i18n.JvmParameterBundle;
import io.github.newhoo.jvm.util.AppUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SettingForm
 *
 * @author huzunrong
 * @since 1.0
 */
public class SettingForm {
    public JPanel mainPanel;

    private JPanel addJvmPanel;
    private JLabel previewLabel;
    private JPanel decorationLayoutPanel;
    public JTextField jvmParameterText;

    private JPanel generateJvmPanel;

    private final Project project;

    private final MyJvmTableModel dataModel = new MyJvmTableModel();
    private final TableModelListener tableModelListener = e -> {
        String jvmParameter = dataModel.list.stream()
                                            .filter(cs -> Objects.equals(true, cs[0]))
                                            .filter(cs -> StringUtils.isNotEmpty(String.valueOf(cs[1])))
                                            .map(cs -> {
                                                String c2 = String.valueOf(cs[2]);
                                                if (StringUtils.isEmpty(c2)) {
                                                    return String.valueOf(cs[1]);
                                                }
                                                return !StringUtils.containsWhitespace(c2)
                                                        ? "-D" + cs[1] + "=" + c2
                                                        : "\"-D" + cs[1] + "=" + c2 + "\"";
                                            })
                                            .collect(Collectors.joining(" "));
        jvmParameterText.setText(jvmParameter);
        jvmParameterText.setToolTipText(jvmParameter);
    };

    public SettingForm(Project project) {
        this.project = project;

        init();
    }

    private void init() {
        previewLabel.setText(JvmParameterBundle.getMessage("label.jvm.parameter.preview"));
        addJvmPanel.setBorder(IdeBorderFactory.createTitledBorder(JvmParameterBundle.getMessage("label.jvm.parameter.add"), false));
        generateJvmPanel.setBorder(IdeBorderFactory.createTitledBorder(JvmParameterBundle.getMessage("label.jvm.parameter.generate"), false));

        dataModel.addTableModelListener(tableModelListener);

        JBTable jbTable = new JBTable(dataModel);
        jbTable.getColumnModel().getColumn(0).setMaxWidth(40);
        ToolbarDecorator decorationToolbar = ToolbarDecorator.createDecorator(jbTable)
                                                             .setAddAction(button -> {
                                                                 EventQueue.invokeLater(dataModel::addRow);
                                                             })
                                                             .setRemoveAction(button -> {
                                                                 EventQueue.invokeLater(() -> {
                                                                     TableUtil.removeSelectedItems(jbTable);
                                                                 });
                                                             });

        decorationLayoutPanel.add(decorationToolbar.createPanel(), BorderLayout.CENTER);

        // 生成快捷按钮
        generateButton();
    }

    private void generateButton() {
        JButton jvmMemoryBtn = new JButton(JvmParameterBundle.getMessage("generate.btn.jvmMem"));
        jvmMemoryBtn.addActionListener(l -> {
            dataModel.addRow(true, "-Xms512m -Xmx512m", "");
        });
        JButton apolloBtn = new JButton(JvmParameterBundle.getMessage("generate.btn.apollo"));
        apolloBtn.addActionListener(l -> {
            dataModel.addRow(true, "env", "DEV");
            dataModel.addRow(false, "idc", "default");
        });
        JButton dubboLocalDevBtn = new JButton(JvmParameterBundle.getMessage("generate.btn.doubleLocal"));
        dubboLocalDevBtn.addActionListener(l -> {
            dataModel.addRow(true, "dubbo.registry.register", "false");
            dataModel.addRow(true, "dubbo.service.group", System.getProperty("user.name"));
        });
        JButton dubboGroupBtn = new JButton(JvmParameterBundle.getMessage("generate.btn.doubleServiceGroup"));
        dubboGroupBtn.addActionListener(l -> {
            AppUtils.findDubboService(this.project).forEach(s -> {
                dataModel.addRow(true, "dubbo.service." + s + ".group", System.getProperty("user.name"));
            });
        });
        generateJvmPanel.add(jvmMemoryBtn);
        generateJvmPanel.add(apolloBtn);
        generateJvmPanel.add(dubboLocalDevBtn);
        generateJvmPanel.add(dubboGroupBtn);
    }

    public String getJvmParameterTableText() {
        return dataModel.list.stream()
                             .flatMap(objects -> Stream.of(objects)
                                                       .map(o -> StringUtils.isEmpty(o.toString()) ? " " : o.toString()))
                             .collect(Collectors.joining("@@@"));
    }

    public void setJvmParameterTableText(String jvmParameterTableText) {
        String[] split = StringUtils.split(jvmParameterTableText, "@@@");
        if (split == null || split.length % 3 != 0) {
            return;
        }

        dataModel.clear();
        for (int i = 0; i < split.length; i = i + 3) {
            dataModel.addRow(BooleanUtils.toBoolean(split[i]), StringUtils.trimToEmpty(split[i + 1]),
                    StringUtils.trimToEmpty(split[i + 2]));
        }
    }
}