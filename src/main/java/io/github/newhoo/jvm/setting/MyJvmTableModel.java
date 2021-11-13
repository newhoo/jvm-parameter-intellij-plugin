package io.github.newhoo.jvm.setting;

import com.intellij.util.ui.EditableModel;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * MyJvmTableModel
 *
 * @author huzunrong
 * @since 1.0
 */
public class MyJvmTableModel extends AbstractTableModel implements EditableModel {

    List<JvmParameter> list = new ArrayList<>();

    private final String[] head = {"ENABLE", "NAME", "VALUE", "GLOBAL"};

    private final Class<?>[] typeArray = {Boolean.class, String.class, String.class, Boolean.class};

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return head.length;
    }

    @Override
    public String getColumnName(int column) {
        return head[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        JvmParameter jvmParameter = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return jvmParameter.getEnabled();
            case 1:
                return jvmParameter.getName();
            case 2:
                return jvmParameter.getValue();
            case 3:
                return jvmParameter.getGlobal();
            default:
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//        list.get(rowIndex)[columnIndex] = aValue;
        JvmParameter jvmParameter = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                jvmParameter.setEnabled((Boolean) aValue);
                break;
            case 1:
                jvmParameter.setName((String) aValue);
                break;
            case 2:
                jvmParameter.setValue((String) aValue);
                break;
            case 3:
                jvmParameter.setGlobal((Boolean) aValue);
                break;
            default:
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return typeArray[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public void addRow(boolean enable, String name, String value) {
        addRow(enable, name, value, false);
    }

    public void addRow(boolean enable, String name, String value, boolean isGlobal) {
//        list.add(new Object[]{enable, name, value, isGlobal});
        list.add(new JvmParameter(enable, name, value, isGlobal));
        fireTableRowsInserted(getRowCount() - 1, getRowCount());
    }

    public void clear() {
        list.clear();
        fireTableDataChanged();
    }

    @Override
    public void addRow() {
//        list.add(new Object[]{true, "", "", false});
        list.add(new JvmParameter(true, "", "", false));
        fireTableRowsInserted(getRowCount() - 1, getRowCount());
    }

    @Override
    public void exchangeRows(int oldIndex, int newIndex) {
//        Object[] objects = list.get(oldIndex);
//        list.set(oldIndex, list.get(newIndex));
//        list.set(newIndex, objects);

        JvmParameter jvmParameter1 = list.get(oldIndex);
        list.set(oldIndex, list.get(newIndex));
        list.set(newIndex, jvmParameter1);

        fireTableDataChanged();
    }

    @Override
    public boolean canExchangeRows(int oldIndex, int newIndex) {
        return true;
    }

    @Override
    public void removeRow(int idx) {
        list.remove(idx);
        fireTableRowsDeleted(0, getRowCount());
    }
}