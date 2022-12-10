package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy;

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

    private UastInspectionStrategy mInspectionStrategy = null;

    public InspectionEditorForm(UastInspectionStrategy inspection) {
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
            data[i] = new Object[]{groupPatterns.get(i)};
        }
        TableModel tableModel = new DefaultTableModel(
                data, new Object[]{"Group patterns"}
        );
        tableModel.addTableModelListener(new GroupPatternsTableModelListener(mInspectionStrategy));
        patternsTable.setModel(tableModel);
    }

    private void populateAddButton() {
        addPatternButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                DefaultTableModel tableModel = (DefaultTableModel) patternsTable.getModel();
                if (tableModel == null) {
                    return;
                }

                List<String> patterns = mInspectionStrategy.getGroupPatterns();
                patterns.add("");
                mInspectionStrategy.setGroupPatterns(patterns);

                tableModel.insertRow(tableModel.getRowCount(), new Object[]{""});
                patternsTable.changeSelection(tableModel.getRowCount() - 1, 0, false, false);
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
