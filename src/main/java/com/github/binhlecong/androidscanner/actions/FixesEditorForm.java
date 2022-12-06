package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FixesEditorForm extends JPanel {
    private JPanel rootPanel;
    private JTable fixesTable;
    private JScrollPane fixesScrollView;
    private JPanel findNReplacePanel;

    public FixesEditorForm(List<ReplaceStrategy> fixes) {
        super();
        populateUI(fixes);
    }

    private void populateUI(List<ReplaceStrategy> fixes) {
        int n = fixes.size();
        Object[][] data = new Object[n][];
        for (int i = 0; i < n; i++) {
            data[i] = getRowData(fixes.get(i));
        }
        fixesTable.setModel(new DefaultTableModel(
                data, new Object[]{"Fix name", "Find and Replace"}
        ));

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
