package com.github.binhlecong.androidscanner.actions;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

public class FindNReplaceTableModelListener implements TableModelListener {
    private final List<String> mFinds;
    private final List<String> mReplaces;

    public FindNReplaceTableModelListener(List<String> finds, List<String> replaces) {
        mFinds = finds;
        mReplaces = replaces;
    }

    @Override
    public void tableChanged(TableModelEvent event) {
        int row = event.getFirstRow();
        int column = event.getColumn();
        TableModel model = (TableModel) event.getSource();
        Object data = model.getValueAt(row, column);
        if (column == 0) {
            mFinds.set(row, (String) data);
        } else if (column == 1) {
            mReplaces.set(row, (String) data);
        }
    }
}
