package io.github.newhoo.jvm.setting;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.ui.TableUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import io.github.newhoo.jvm.i18n.JvmParameterBundle;
import io.github.newhoo.jvm.util.AppUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SettingForm
 *
 * @author huzunrong
 * @since 1.0
 */
public class SettingForm {

    public JPanel mainPanel;

    private JLabel previewLabel;
    private JTextField jvmParameterText;
    private JPanel decorationPanel;

    private final Project project;

    private final MyJvmTableModel dataModel = new MyJvmTableModel();

    public SettingForm(Project project) {
        this.project = project;

        init();
    }

    private void init() {
        dataModel.addTableModelListener(e -> {
            List<String> list = dataModel.list.stream()
                                              .map(JvmParameter::toRunParameter)
                                              .filter(StringUtils::isNotEmpty)
                                              .collect(Collectors.toList());
            jvmParameterText.setText(String.join(" ", list));
            jvmParameterText.setToolTipText(String.join("<br/>", list));
        });

        previewLabel.setText(JvmParameterBundle.getMessage("label.jvm.parameter.preview"));
        previewLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 生成快捷按钮
                generateButton(e);
            }
        });
        jvmParameterText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    generateButton2(e);
                }
            }
        });

        JBTable jbTable = new JBTable(dataModel);
        jbTable.getColumnModel().getColumn(0).setMaxWidth(100);
        jbTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        jbTable.getColumnModel().getColumn(3).setMaxWidth(100);
        jbTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        ToolbarDecorator decorationToolbar = ToolbarDecorator.createDecorator(jbTable)
                                                             .setAddAction(button -> {
                                                                 EventQueue.invokeLater(dataModel::addRow);
                                                             })
                                                             .setRemoveAction(button -> {
                                                                 EventQueue.invokeLater(() -> {
                                                                     TableUtil.removeSelectedItems(jbTable);
                                                                 });
                                                             });
        decorationPanel.add(decorationToolbar.createPanel(), BorderLayout.CENTER);
    }

    private void generateButton(MouseEvent e) {
        DefaultActionGroup generateActionGroup = new DefaultActionGroup(
                new AnAction(JvmParameterBundle.getMessage("generate.btn.jvmMem")) {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        dataModel.addRow(true, "-Xms512m -Xmx512m", "");
                    }
                },
                new AnAction(JvmParameterBundle.getMessage("generate.btn.apollo")) {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        dataModel.addRow(true, "env", "DEV");
                        dataModel.addRow(false, "idc", "default");
                    }
                },
                new AnAction(JvmParameterBundle.getMessage("generate.btn.doubleLocal")) {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        dataModel.addRow(true, "dubbo.registry.register", "false");
                        dataModel.addRow(true, "dubbo.service.group", System.getProperty("user.name"));
                    }
                },
                new AnAction(JvmParameterBundle.getMessage("generate.btn.doubleServiceGroup")) {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        ApplicationManager.getApplication().runReadAction(() -> {
                            AppUtils.findDubboService(project).forEach(s -> {
                                dataModel.addRow(true, "dubbo.service." + s + ".group", System.getProperty("user.name"));
                            });
                        });
                    }
                }
        );

        DataContext dataContext = DataManager.getInstance().getDataContext(e.getComponent());
        final ListPopup popup = JBPopupFactory.getInstance()
                                              .createActionGroupPopup(
                                                      JvmParameterBundle.getMessage("label.jvm.parameter.generate"),
                                                      generateActionGroup,
                                                      dataContext,
                                                      JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                                                      true);
        popup.showInBestPositionFor(dataContext);
    }

    private void generateButton2(MouseEvent e) {
        DefaultActionGroup generateActionGroup = new DefaultActionGroup(
                new AnAction("Copy as line string") {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        String text = jvmParameterText.getText();
                        if (StringUtils.isNotEmpty(text)) {
                            String s = text.replace("\\", "\\\\");
                            CopyPasteManager.getInstance().setContents(new StringSelection(s));
                        }
                    }
                },
                new AnAction("Copy as multiline string") {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        String s = dataModel.list.stream()
                                                 .map(JvmParameter::toRunParameter)
                                                 .filter(StringUtils::isNotEmpty)
                                                 .collect(Collectors.joining("\n"));
                        if (StringUtils.isNotEmpty(s)) {
                            s = s.replace("\\", "\\\\");
                            CopyPasteManager.getInstance().setContents(new StringSelection(s));
                        }
                    }
                },
                new AnAction("Copy as string array") {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        String s = dataModel.list.stream()
                                                 .map(JvmParameter::toRunParameter2)
                                                 .filter(StringUtils::isNotEmpty)
                                                 .collect(Collectors.joining(",\n    "));
                        if (StringUtils.isNotEmpty(s)) {
                            s = s.replace("\\", "\\\\");
                            CopyPasteManager.getInstance().setContents(new StringSelection("[\n    " + s + "\n]"));
                        }
                    }
                }
        );
        DataContext dataContext = DataManager.getInstance().getDataContext(jvmParameterText);
        final ListPopup popup = JBPopupFactory.getInstance()
                                              .createActionGroupPopup(
                                                      null,
                                                      generateActionGroup,
                                                      dataContext,
                                                      JBPopupFactory.ActionSelectionAid.SPEEDSEARCH,
                                                      true);
        popup.showInBestPositionFor(dataContext);
    }

    public Pair<JvmParameterSetting, JvmParameterSetting> getModifiedSetting() {
        JvmParameterSetting globalJvmParameterSetting = new JvmParameterSetting();
        JvmParameterSetting projectJvmParameterSetting = new JvmParameterSetting();
        saveTo(globalJvmParameterSetting, projectJvmParameterSetting);
        return Pair.of(globalJvmParameterSetting, projectJvmParameterSetting);
    }

    public void saveTo(JvmParameterSetting globalJvmParameterSetting, JvmParameterSetting projectJvmParameterSetting) {
        List<JvmParameter> globalParameter = dataModel.list.stream()
                                                           .filter(jvmParameter -> jvmParameter.getGlobal())
                                                           .collect(Collectors.toList());
        globalJvmParameterSetting.setJvmParameterList(globalParameter);
        List<JvmParameter> parameter = dataModel.list.stream()
                                                     .filter(jvmParameter -> !jvmParameter.getGlobal())
                                                     .collect(Collectors.toList());
        projectJvmParameterSetting.setJvmParameterList(parameter);
    }

    public void reset(JvmParameterSetting globalJvmParameterSetting, JvmParameterSetting projectJvmParameterSetting) {
        dataModel.clear();

        for (JvmParameter jvmParameter : globalJvmParameterSetting.getJvmParameterList()) {
            dataModel.addRow(BooleanUtils.toBooleanDefaultIfNull(jvmParameter.getEnabled(), false),
                    jvmParameter.getName(), jvmParameter.getValue(),
                    BooleanUtils.toBooleanDefaultIfNull(jvmParameter.getGlobal(), false));
        }
        for (JvmParameter jvmParameter : projectJvmParameterSetting.getJvmParameterList()) {
            dataModel.addRow(BooleanUtils.toBooleanDefaultIfNull(jvmParameter.getEnabled(), false),
                    jvmParameter.getName(), jvmParameter.getValue(),
                    BooleanUtils.toBooleanDefaultIfNull(jvmParameter.getGlobal(), false));
        }
    }
}