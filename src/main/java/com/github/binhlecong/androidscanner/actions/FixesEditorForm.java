package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FixesEditorForm extends JPanel {
    private JPanel rootPanel;
    private JTable fixesTable;
    private JScrollPane fixesScrollView;
    private JPanel findNReplacePanel;
    private JButton addLintFixButton;
    private JButton deleteLintFixButton;

    public FixesEditorForm(List<ReplaceStrategy> fixes) {
        super();
        populateUI(fixes);
        populateAddButton(fixes);
        populateDeleteButton(fixes);
    }

    private void populateUI(List<ReplaceStrategy> fixes) {
        int n = fixes.size();
        Object[][] data = new Object[n][];
        for (int i = 0; i < n; i++) {
            data[i] = getRowData(fixes.get(i));
        }
        TableModel tableModel = new DefaultTableModel(
                data, new Object[]{"Fix name*", "Find and Replace*"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 1;
            }
        };
        tableModel.addTableModelListener(new FixesTableModelListener(fixes));
        fixesTable.setModel(tableModel);

        fixesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                int row = fixesTable.rowAtPoint(event.getPoint());
                int col = fixesTable.columnAtPoint(event.getPoint());
                if (row < 0 || col < 0) return;
                if (col == 1) {
                    JPanel fixesEditorForm = new FindReplaceForm(fixes.get(row).getPatterns(), fixes.get(row).getStrings()).getRootPanel();
                    populateEditor(fixesEditorForm);
                } else {
                    populateEditor(null);
                }
            }
        });
    }

    private void populateAddButton(List<ReplaceStrategy> fixes) {
        addLintFixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                DefaultTableModel tableModel = (DefaultTableModel) fixesTable.getModel();
                if (tableModel == null) {
                    return;
                }

                ReplaceStrategy newReplaceStrategy = new ReplaceStrategy(
                        "",
                        new ArrayList<>(0),
                        new ArrayList<>(0));
                fixes.add(newReplaceStrategy);

                tableModel.insertRow(tableModel.getRowCount(), getRowData(newReplaceStrategy));
                fixesTable.changeSelection(tableModel.getRowCount() - 1, 0, false, false);
            }
        });
    }

    private void populateDeleteButton(List<ReplaceStrategy> fixes) {
        deleteLintFixButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DefaultTableModel tableModel = (DefaultTableModel) fixesTable.getModel();
                if (tableModel == null) {
                    return;
                }

                int row = fixesTable.getSelectedRow();
                if (row == -1) {
                    return;
                }

                fixes.remove(row);
                tableModel.removeRow(row);
            }
        });
    }

    private Object[] getRowData(ReplaceStrategy fix) {
        return new Object[]{fix.getFixName(), "..."};
    }

    private void populateEditor(Component component) {
        findNReplacePanel.setLayout(new java.awt.BorderLayout());
        findNReplacePanel.removeAll();
        if (component != null) {
            findNReplacePanel.add(component);
        }
        findNReplacePanel.validate();
        findNReplacePanel.repaint();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
