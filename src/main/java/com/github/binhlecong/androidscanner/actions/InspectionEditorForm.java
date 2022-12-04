package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class InspectionEditorForm extends JPanel {
    private JPanel rootPanel;
    private JTable patternsTable;
    private JPanel textFieldPanel;
    private JPanel scrollViewPanel;
    private JScrollPane patternsScrollView;
    private JFormattedTextField patternTextField;

    public InspectionEditorForm(UastInspectionStrategy inspection) {
        super();
        populateUI(inspection);
    }

    private void populateUI(UastInspectionStrategy inspection) {
        patternTextField.setText(inspection.getPattern());

        List<String> groupPatterns = inspection.getGroupPatterns();
        int n = groupPatterns.size();
        Object[][] data = new Object[n][];
        for (int i = 0; i < n; i++) {
            data[i] = new Object[]{groupPatterns.get(i)};
        }
        patternsTable.setModel(new DefaultTableModel(
                data, new Object[]{"Group patterns",}
        ));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
