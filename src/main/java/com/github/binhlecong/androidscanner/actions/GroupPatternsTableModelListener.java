package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.rules.Inspection;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class GroupPatternsTableModelListener implements TableModelListener {
    private final Inspection mInspectionStrategy;

    GroupPatternsTableModelListener(Inspection inspection) {
        mInspectionStrategy = inspection;
    }

    @Override
    public void tableChanged(TableModelEvent event) {
        if (mInspectionStrategy == null) {
            return;
        }
        int row = event.getFirstRow();
        int column = event.getColumn();
        if (row == -1 || column == -1) {
            return;
        }
        TableModel model = (TableModel) event.getSource();
        Object data = model.getValueAt(row, column);
        mInspectionStrategy.getGroupPatterns().set(row, (String) data);
    }
}
