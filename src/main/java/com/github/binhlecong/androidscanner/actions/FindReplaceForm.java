package com.github.binhlecong.androidscanner.actions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FindReplaceForm extends JPanel {
    private JTable findNReplaceTable;
    private JPanel rootPanel;
    private JScrollPane findNReplaceScrollView;
    private JButton addFindReplaceButton;
    private JButton deleteFindReplaceButton;

    public FindReplaceForm(List<String> finds, List<String> replaces) {
        super();
        populateUI(finds, replaces);
        populateAddButton(finds, replaces);
    }

    private void populateUI(List<String> finds, List<String> replaces) {
        int n = finds.size();
        Object[][] data = new Object[n][];
        for (int i = 0; i < n; i++) {
            data[i] = new Object[]{finds.get(i), replaces.get(i)};
        }
        TableModel tableModel = new DefaultTableModel(
                data, new Object[]{"Find", "Replace"}
        );
        tableModel.addTableModelListener(new FindNReplaceTableModelListener(finds, replaces));
        findNReplaceTable.setModel(tableModel);
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

                tableModel.insertRow(tableModel.getRowCount(), new Object[]{"", ""});
                findNReplaceTable.changeSelection(tableModel.getRowCount() - 1, 0, false, false);
            }
        });
    }

    private void populateDeleteButton(List<String> finds, List<String> replaces) {
        deleteFindReplaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DefaultTableModel tableModel = (DefaultTableModel) findNReplaceTable.getModel();
                if (tableModel == null) {
                    return;
                }

                int row = findNReplaceTable.getSelectedRow();
                if (row == -1) {
                    return;
                }

                finds.remove(row);
                replaces.remove(row);
                tableModel.removeRow(row);
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
