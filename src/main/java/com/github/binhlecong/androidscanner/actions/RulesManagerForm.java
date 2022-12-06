package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.Config;
import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy;
import com.github.binhlecong.androidscanner.rules.JavaRule;
import com.github.binhlecong.androidscanner.rules.KotlinRule;
import com.github.binhlecong.androidscanner.rules.Rule;
import com.github.binhlecong.androidscanner.rules.RulesManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// https://plugins.jetbrains.com/docs/intellij/dialog-wrapper.html#example
public class RulesManagerForm extends DialogWrapper {
    private JComboBox<String> selectLangDropdown;
    private JLabel selectLangLabel;
    private JScrollPane rulesScrollView;
    private JTable rulesTable;
    private JButton addRuleButton;
    private JPanel rootPanel;
    private JPanel editorPanel;

    final private String[] mLanguageOptions = Config.Companion.getRULES_FILES();
    private String mLanguageSelected = mLanguageOptions[0];
    private Rule<UastInspectionStrategy>[] mRules = null;

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
        // Get current state of all tables
        // Call rules manager to save rules
        switch (mLanguageSelected) {
            case "java.json":
                RulesManager.INSTANCE.saveJavaRules((JavaRule[]) mRules);
                break;
            case "kotlin.json":
                RulesManager.INSTANCE.saveKotlinRules((KotlinRule[]) mRules);
                break;
            default:
                break;
        }
        super.doOKAction();
    }

    private void populateDialog() {
        populateDropdownList();
        populateTable(mLanguageSelected);
    }

    private void populateDropdownList() {
        for (String option : mLanguageOptions) {
            selectLangDropdown.addItem(option);
        }
        selectLangDropdown.setSelectedIndex(0);
        selectLangDropdown.addActionListener(event -> {
            JComboBox<String> cb = (JComboBox<String>) event.getSource();
            mLanguageSelected = (String) cb.getSelectedItem();
            populateTable(mLanguageSelected);
        });
    }

    private void populateTable(String language) {
        if (language == null) {
            mRules = RulesManager.INSTANCE.cloneJavaRules();
        } else {
            switch (language) {
                case "java.json":
                    mRules = RulesManager.INSTANCE.cloneJavaRules();
                    break;
                case "kotlin.json":
                    mRules = RulesManager.INSTANCE.cloneKotlinRules();
                    break;
                default:
                    mRules = RulesManager.INSTANCE.cloneJavaRules();
                    break;
            }
        }
        Object[][] data = new Object[mRules.length][];
        for (int i = 0; i < mRules.length; i++) {
            data[i] = getRowData(mRules[i]);
        }
        rulesTable.setModel(new DefaultTableModel(
                data, new Object[]{"ID", "Brief description", "Inspection", "Fixes", "Highlight type", "Enabled"}
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Display the "Enable" column as checkbox
                if (columnIndex == 5) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
        });

        TableColumnModel columnsModel = rulesTable.getColumnModel();
        columnsModel.getColumn(0).setMinWidth(150);
        columnsModel.getColumn(1).setMinWidth(400);
        columnsModel.getColumn(2).setMaxWidth(100);
        columnsModel.getColumn(3).setMaxWidth(100);
        columnsModel.getColumn(4).setMinWidth(100);
        columnsModel.getColumn(5).setMaxWidth(100);

        rulesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                int row = rulesTable.rowAtPoint(event.getPoint());
                int col = rulesTable.columnAtPoint(event.getPoint());
                if (row < 0 || col < 0) return;
                if (col == 2) {
                    JPanel inspectionEditorForm = new InspectionEditorForm(mRules[row].getInspector()).getRootPanel();
                    populateEditor(inspectionEditorForm);
                } else if (col == 3) {
                    JPanel fixesEditorForm = new FixesEditorForm(mRules[row].getFixes()).getRootPanel();
                    populateEditor(fixesEditorForm);
                } else {
                    populateEditor(null);
                }
            }
        });
    }

    private void populateEditor(Component component) {
        editorPanel.setLayout(new java.awt.BorderLayout());
        editorPanel.removeAll();
        if (component != null) {
            editorPanel.add(component);
        }
        editorPanel.validate();
        editorPanel.repaint();
    }

    private Object[] getRowData(Rule<UastInspectionStrategy> rule) {
        return new Object[]{rule.getId(), rule.getBriefDescription(), "...", "...", rule.getHighlightType(), true};
    }
}
