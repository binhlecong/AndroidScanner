package com.github.binhlecong.androidscanner.actions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class FindReplaceForm extends JPanel {
    private JTable findNReplaceTable;
    private JPanel rootPanel;
    private JScrollPane findNReplaceScrollView;

    public FindReplaceForm(List<String> finds, List<String> replaces) {
        super();
        populateUI(finds, replaces);
    }

    private void populateUI(List<String> finds, List<String> replaces) {
        int n = finds.size();
        Object[][] data = new Object[n][];
        for (int i = 0; i < n; i++) {
            data[i] = new Object[]{finds.get(i), replaces.get(i)};
        }
        findNReplaceTable.setModel(new DefaultTableModel(
                data, new Object[]{"Find", "Replace"}
        ));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
