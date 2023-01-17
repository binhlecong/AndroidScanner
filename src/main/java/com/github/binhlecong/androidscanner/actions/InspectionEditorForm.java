package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.rules.Inspection;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
        populateDeleteButton();
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
            data[i] = new Object[]{groupPatterns.get(i)};
        }
        TableModel tableModel = new DefaultTableModel(
                data, new Object[]{"Group patterns"}
        );
        tableModel.addTableModelListener(new GroupPatternsTableModelListener(mInspectionStrategy));
        patternsTable.setModel(tableModel);
    }

    private void populateAddButton() {
        addPatternButton.addActionListener(event -> {
            DefaultTableModel tableModel = (DefaultTableModel) patternsTable.getModel();
            if (tableModel == null) {
                return;
            }

            mInspectionStrategy.getGroupPatterns().add("");

            tableModel.insertRow(tableModel.getRowCount(), new Object[]{""});
            patternsTable.changeSelection(tableModel.getRowCount() - 1, 0, false, false);
        });
    }

    private void populateDeleteButton() {
        deleteArgumentPatternsButton.addActionListener(actionEvent -> {
            DefaultTableModel tableModel = (DefaultTableModel) patternsTable.getModel();
            if (tableModel == null) {
                return;
            }

            int row = patternsTable.getSelectedRow();
            if (row == -1) {
                return;
            }

            mInspectionStrategy.getGroupPatterns().remove(row);
            tableModel.removeRow(row);
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
