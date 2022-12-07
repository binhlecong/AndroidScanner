package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

public class GroupPatternsTableModelListener implements TableModelListener {
    private UastInspectionStrategy mInspectionStrategy;
    private List<String> mGroupPatterns;

    GroupPatternsTableModelListener(UastInspectionStrategy inspectionStrategy) {
        mInspectionStrategy = inspectionStrategy;
        mGroupPatterns = new ArrayList<>(mInspectionStrategy.getGroupPatterns());
    }

    @Override
    public void tableChanged(TableModelEvent event) {
        if (mInspectionStrategy == null) {
            return;
        }
        int row = event.getFirstRow();
        int column = event.getColumn();
        TableModel model = (TableModel) event.getSource();
        Object data = model.getValueAt(row, column);
        mGroupPatterns.set(row, (String) data);
        mInspectionStrategy.setGroupPatterns(mGroupPatterns);
    }
}
