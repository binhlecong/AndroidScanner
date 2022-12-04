package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy;
import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy;
import com.github.binhlecong.androidscanner.rules.Rule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.util.List;

public class FixesEditorForm extends JPanel {
    private JPanel rootPanel;
    private JTable fixesTable;
    private JScrollPane fixesScrollView;

    public FixesEditorForm(List<ReplaceStrategy> fixes){
        super();
        populateUI(fixes);
    }

    public void populateUI(List<ReplaceStrategy> fixes){
        int n = fixes.size();
        Object[][] data = new Object[n][];
        for (int i = 0; i < n; i++) {
            data[i] = getRowData(fixes.get(i));
        }
        fixesTable.setModel(new DefaultTableModel(
                data, new Object[]{"ID", "Brief description"}
        ));
    }

    private Object[] getRowData(ReplaceStrategy fix) {
        return new Object[]{fix.getName(),  "....."};
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
