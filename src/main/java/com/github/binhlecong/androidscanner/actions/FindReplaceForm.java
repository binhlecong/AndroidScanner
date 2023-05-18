package com.github.binhlecong.androidscanner.actions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static icons.MyIcons.DeleteIcon;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class FindReplaceForm extends JPanel {
    private JTable findNReplaceTable;
    private JPanel rootPanel;
    private JScrollPane findNReplaceScrollView;
    private JButton addFindReplaceButton;

    public FindReplaceForm(List<String> finds, List<String> replaces) {
        super();
        populateUI(finds, replaces);
        populateAddButton(finds, replaces);
    }

    private void populateUI(List<String> finds, List<String> replaces) {
        int n = finds.size();
        Object[][] data = new Object[n][];
        for (int i = 0; i < n; i++) {
            data[i] = new Object[]{finds.get(i), replaces.get(i), DeleteIcon};
        }
        TableModel tableModel = new DefaultTableModel(
                data, new Object[]{"Find", "Replace", ""}
        ){
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Display the "Enable" column as checkbox
                if (columnIndex == 2) {
                    return ImageIcon.class;
                }
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 2;
            }
        };
        tableModel.addTableModelListener(new FindNReplaceTableModelListener(finds, replaces));
        findNReplaceTable.setModel(tableModel);
        TableColumnModel columnsModel = findNReplaceTable.getColumnModel();
        columnsModel.getColumn(2).setMaxWidth(100);

        findNReplaceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                int row = findNReplaceTable.rowAtPoint(event.getPoint());
                int col = findNReplaceTable.columnAtPoint(event.getPoint());
                if (row < 0 || col < 0) return;
                if (col == 2) {
                    int confirmMessage = JOptionPane.showInternalConfirmDialog(null, "Do you want to delete this find replace?", "Confirm delete find replace", YES_NO_OPTION, QUESTION_MESSAGE);
                    if (confirmMessage == 0){
                        DefaultTableModel tableModel = (DefaultTableModel) findNReplaceTable.getModel();
                        if (tableModel == null) {
                            return;
                        }

                        if (row == -1) {
                            return;
                        }

                        finds.remove(row);
                        replaces.remove(row);
                        tableModel.removeRow(row);
                    }
                }

            }
        });
    }

    private void populateAddButton(List<String> finds, List<String> replaces) {
        addFindReplaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                DefaultTableModel tableModel = (DefaultTableModel) findNReplaceTable.getModel();
                if (tableModel == null) {
                    return;
                }

                finds.add("");
                replaces.add("");

                tableModel.insertRow(tableModel.getRowCount(), new Object[]{"", "", DeleteIcon});
                findNReplaceTable.changeSelection(tableModel.getRowCount() - 1, 0, false, false);
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
