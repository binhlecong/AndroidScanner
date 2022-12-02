package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy;
import com.github.binhlecong.androidscanner.rules.Rule;
import com.github.binhlecong.androidscanner.rules.RulesManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RulesManagerForm extends DialogWrapper {
    private JComboBox selectLangDropdown;
    private JLabel selectLangLabel;
    private JScrollPane rulesScrollView;
    private JScrollPane inspectorScrollView;
    private JTable rulesTable;
    private JButton addRuleButton;
    private JPanel rootPanel;

    public RulesManagerForm() {
        super(true);
        setTitle("Rules Manager");
        populateTable();
    }

    private void populateTable() {
        Rule<UastInspectionStrategy>[] rules = RulesManager.INSTANCE.getJavaRules();
        Object[][] data = new Object[rules.length][];
        for (int i = 0; i < rules.length; i++) {
            data[i] = getRowData(rules[i]);
        }
        rulesTable.setModel(new DefaultTableModel(
                data, new Object[]{"ID", "Brief description", "Inspection", "Fixes", "Highlight type", "Enabled"}
        ));
    }

    private Object[] getRowData(Rule<UastInspectionStrategy> rule) {
        return new Object[]{rule.getId(), rule.getBriefDescription(), "...", "...", rule.getHighlightType(), true};
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return rootPanel;
    }

    @Override
    protected void doOKAction() {
        // todo: save rules
        super.doOKAction();
    }
}
