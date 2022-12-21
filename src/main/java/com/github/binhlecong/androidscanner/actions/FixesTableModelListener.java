package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

public class FixesTableModelListener implements TableModelListener {
    private final List<ReplaceStrategy> mFixes;

    public FixesTableModelListener(List<ReplaceStrategy> fixes) {
        mFixes = fixes;
    }

    @Override
    public void tableChanged(TableModelEvent event) {
        int row = event.getFirstRow();
        int column = event.getColumn();
        if (row == -1 || column == -1) {
            return;
        }
        TableModel model = (TableModel) event.getSource();
        Object data = model.getValueAt(row, column);
        if (column == 0) {
            mFixes.get(row).setFixName((String) data);
        }
    }
}
