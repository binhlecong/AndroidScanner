package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.Config;
import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy;
import com.github.binhlecong.androidscanner.rules.Rule;
import com.github.binhlecong.androidscanner.rules.RulesManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

// https://plugins.jetbrains.com/docs/intellij/dialog-wrapper.html#example
public class RulesManagerForm extends DialogWrapper {
    private JComboBox<String> selectLangDropdown;
    private JLabel selectLangLabel;
    private JScrollPane rulesScrollView;
    private JScrollPane inspectorScrollView;
    private JTable rulesTable;
    private JButton addRuleButton;
    private JPanel rootPanel;

    final private String[] mLanguageOptions = Config.Companion.getRULES_FILES();

    public RulesManagerForm(@Nullable Project project) {
        super(project);
        setTitle("Rules Manager");
        init();
        populateDialog();
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

    private void populateDialog() {
        populateDropdownList();
        populateTable(mLanguageOptions[0]);
    }

    private void populateDropdownList() {
        for (String option : mLanguageOptions) {
            selectLangDropdown.addItem(option);
        }
        selectLangDropdown.setSelectedIndex(0);
        selectLangDropdown.addActionListener(event -> {
            JComboBox<String> cb = (JComboBox<String>) event.getSource();
            String selectedItem = (String) cb.getSelectedItem();
            populateTable(selectedItem);
        });
    }

    private void populateTable(String language) {
        Rule<UastInspectionStrategy>[] rules;
        switch (language) {
            case "java.json":
                rules = RulesManager.INSTANCE.getJavaRules();
                break;
            case "kotlin.json":
                rules = RulesManager.INSTANCE.getKotlinRules();
                break;
            default:
                rules = RulesManager.INSTANCE.getJavaRules();
                break;
        }

        Object[][] data = new Object[rules.length][];
        for (int i = 0; i < rules.length; i++) {
            data[i] = getRowData(rules[i]);
        }
        rulesTable.setModel(new DefaultTableModel(
                data, new Object[]{"ID", "Brief description", "Inspection", "Fixes", "Highlight type", "Enabled"}
        ));

        TableColumnModel columnsModel = rulesTable.getColumnModel();
        columnsModel.getColumn(0).setMinWidth(200);
        columnsModel.getColumn(1).setMinWidth(400);
        columnsModel.getColumn(2).setMaxWidth(100);
        columnsModel.getColumn(3).setMaxWidth(100);
        columnsModel.getColumn(4).setMaxWidth(120);
        columnsModel.getColumn(5).setMaxWidth(100);
    }

    private Object[] getRowData(Rule<UastInspectionStrategy> rule) {
        return new Object[]{rule.getId(), rule.getBriefDescription(), "...", "...", rule.getHighlightType(), true};
    }
}
