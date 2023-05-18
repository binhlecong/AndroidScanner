package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static icons.MyIcons.DeleteIcon;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class FixesEditorForm extends JPanel {
    private JPanel rootPanel;
    private JTable fixesTable;
    private JScrollPane fixesScrollView;
    private JPanel findNReplacePanel;
    private JButton addLintFixButton;

    public FixesEditorForm(List<ReplaceStrategy> fixes) {
        super();
        populateUI(fixes);
        populateAddButton(fixes);
        if (fixes != null) {
            if (fixes.size() > 0) {
                JPanel fixesEditorForm = new FindReplaceForm(fixes.get(0).getPatterns(), fixes.get(0).getStrings()).getRootPanel();
                populateEditor(fixesEditorForm);
                fixesTable.setRowSelectionInterval(0, 0);
            }
        }
    }

    private void populateUI(List<ReplaceStrategy> fixes) {
        int n = fixes.size();
        Object[][] data = new Object[n][];
        for (int i = 0; i < n; i++) {
            data[i] = getRowData(fixes.get(i));
        }
        TableModel tableModel = new DefaultTableModel(
                data, new Object[]{"Fix name*", "Find and Replace*", ""}
        ) {
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
                return column != 1;
            }
        };
        tableModel.addTableModelListener(new FixesTableModelListener(fixes));
        fixesTable.setModel(tableModel);
        TableColumnModel columnsModel = fixesTable.getColumnModel();
        columnsModel.getColumn(2).setMaxWidth(100);

        fixesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                int row = fixesTable.rowAtPoint(event.getPoint());
                int col = fixesTable.columnAtPoint(event.getPoint());
                if (row < 0 || col < 0) return;
                if (col == 0 || col == 1) {
                    JPanel fixesEditorForm = new FindReplaceForm(fixes.get(row).getPatterns(), fixes.get(row).getStrings()).getRootPanel();
                    populateEditor(fixesEditorForm);
                }
                if (col == 2) {
                    int confirmMessage = JOptionPane.showInternalConfirmDialog(null, "Do you want to delete this lint fix?", "Confirm delete lint fix", YES_NO_OPTION, QUESTION_MESSAGE);
                    if (confirmMessage == 0) {
                        DefaultTableModel tableModel = (DefaultTableModel) fixesTable.getModel();
                        if (tableModel == null) {
                            return;
                        }

                        if (row == -1) {
                            return;
                        }

                        fixes.remove(row);
                        tableModel.removeRow(row);
                    }
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

    private Object[] getRowData(ReplaceStrategy fix) {
        return new Object[]{fix.getFixName(), "...", DeleteIcon};
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
