package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.rules.Inspection;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

public class InspectionEditorForm extends JPanel {
    private JPanel rootPanel;
    private JTable patternsTable;
    private JPanel textFieldPanel;
    private JPanel scrollViewPanel;
    private JScrollPane patternsScrollView;
    private JFormattedTextField patternTextField;
    private JButton addPatternButton;
    private JButton deleteArgumentPatternsButton;

    private Inspection mInspectionStrategy = null;

    public InspectionEditorForm(Inspection inspection) {
        super();
        mInspectionStrategy = inspection;
        populateUI();
        populateAddButton();
    }

    private void populateUI() {
        patternTextField.setText(mInspectionStrategy.getPattern());
        patternTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                update(documentEvent);
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                update(documentEvent);
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                update(documentEvent);
            }

            void update(DocumentEvent event) {
                mInspectionStrategy.setPattern(patternTextField.getText());
            }
        });

        List<String> groupPatterns = mInspectionStrategy.getGroupPatterns();
        int n = groupPatterns.size();
        Object[][] data = new Object[n][];
        for (int i = 0; i < n; i++) {
            data[i] = new Object[]{groupPatterns.get(i), DeleteIcon};
        }
        TableModel tableModel = new DefaultTableModel(
                data, new Object[]{"Group patterns", ""}
        ){
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Display the "Enable" column as checkbox
                if (columnIndex == 1) {
                    return ImageIcon.class;
                }
                return super.getColumnClass(columnIndex);
            }



            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        tableModel.addTableModelListener(new GroupPatternsTableModelListener(mInspectionStrategy));
        patternsTable.setModel(tableModel);
        TableColumnModel columnsModel = patternsTable.getColumnModel();
        columnsModel.getColumn(1).setMaxWidth(100);

        patternsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                int row = patternsTable.rowAtPoint(event.getPoint());
                int col = patternsTable.columnAtPoint(event.getPoint());
                if (row < 0 || col < 0) return;
                if (col == 1) {
                    int confirmMessage = JOptionPane.showInternalConfirmDialog(null, "Do you want to delete this argument pattern?", "Confirm delete argument pattern", YES_NO_OPTION, QUESTION_MESSAGE);
                    if (confirmMessage == 0){
                        DefaultTableModel tableModel = (DefaultTableModel) patternsTable.getModel();
                        if (tableModel == null) {
                            return;
                        }

                        if (row == -1) {
                            return;
                        }

                        mInspectionStrategy.getGroupPatterns().remove(row);
                        tableModel.removeRow(row);
                    }
                }

            }
        });
    }

    private void populateAddButton() {
        addPatternButton.addActionListener(event -> {
            DefaultTableModel tableModel = (DefaultTableModel) patternsTable.getModel();
            if (tableModel == null) {
                return;
            }

            mInspectionStrategy.getGroupPatterns().add("");

            tableModel.insertRow(tableModel.getRowCount(), new Object[]{"", DeleteIcon});
            patternsTable.changeSelection(tableModel.getRowCount() - 1, 0, false, false);
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
