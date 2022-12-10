package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.rules.UastRule;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

class RulesTableModelListener implements TableModelListener {
    private final ArrayList<UastRule> mRules;

    RulesTableModelListener(ArrayList<UastRule> rules) {
        mRules = rules;
    }

    @Override
    public void tableChanged(TableModelEvent event) {
        if (mRules == null) {
            return;
        }
        int row = event.getFirstRow();
        int column = event.getColumn();
        if (row == -1 || column == -1) {
            return;
        }
        TableModel model = (TableModel) event.getSource();
        Object data = model.getValueAt(row, column);
        switch (column) {
            case 0:
                mRules.get(row).setId((String) data);
                break;
            case 1:
                mRules.get(row).setBriefDescription((String) data);
                break;
            // Changes in 'Inspection' and 'Fixes' column is handle elsewhere
            case 4:
                mRules.get(row).setHighlightType((String) data);
                break;
            case 5:
                mRules.get(row).setEnabled((boolean) data);
                break;
            default:
                break;
        }
    }
}