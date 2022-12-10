package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy;
import com.github.binhlecong.androidscanner.rules.Rule;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

class RulesTableModelListener implements TableModelListener {
    private final Rule<UastInspectionStrategy>[] mRules;

    RulesTableModelListener(Rule<UastInspectionStrategy>[] rules) {
        mRules = rules;
    }

    @Override
    public void tableChanged(TableModelEvent event) {
        if (mRules == null) {
            return;
        }
        int row = event.getFirstRow();
        int column = event.getColumn();
        TableModel model = (TableModel) event.getSource();
        Object data = model.getValueAt(row, column);
        switch (column) {
            case 0:
                mRules[row].setId((String) data);
                break;
            case 1:
                mRules[row].setBriefDescription((String) data);
                break;
            // Changes in 'Inspection' and 'Fixes' column is handle elsewhere
            case 4:
                mRules[row].setHighlightType((String) data);
                break;
            case 5:
                mRules[row].setEnabled((boolean) data);
                break;
            default:
                break;
        }
    }
}