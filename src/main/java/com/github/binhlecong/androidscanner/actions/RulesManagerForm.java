package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.Config;
import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy;
import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy;
import com.github.binhlecong.androidscanner.rules.RulesManager;
import com.github.binhlecong.androidscanner.rules.UastRule;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
    private ArrayList<UastRule> mRules = null;

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
        // Decide whether to save rules to java.json or kotlin.json
        switch (mLanguageSelected) {
            case "java.json":
                RulesManager.INSTANCE.saveJavaRules(mRules);
                break;
            case "kotlin.json":
                RulesManager.INSTANCE.saveKotlinRules(mRules);
                break;
            default:
                break;
        }
        super.doOKAction();
    }

    private void populateDialog() {
        populateDropdownList();
        populateTable(mLanguageSelected);
        populateAddButton();
    }

    private void populateAddButton() {
        addRuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                DefaultTableModel tableModel = (DefaultTableModel) rulesTable.getModel();
                if (tableModel == null) {
                    return;
                }

                UastRule newRule = new UastRule(
                        "",
                        "",
                        new UastInspectionStrategy("", new ArrayList<String>(0)),
                        new ArrayList<ReplaceStrategy>(),
                        "WARNING",
                        true);
                mRules.add(newRule);

                Object[] newRowData = getRowData(newRule);
                tableModel.insertRow(tableModel.getRowCount(), newRowData);
                rulesTable.changeSelection(tableModel.getRowCount() - 1, 0, false, false);

                JPanel inspectionEditorForm = new InspectionEditorForm(
                        new UastInspectionStrategy("", new ArrayList<String>())
                ).getRootPanel();
                populateEditor(inspectionEditorForm);
            }
        });
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
        Object[][] data = new Object[mRules.size()][];
        for (int i = 0; i < mRules.size(); i++) {
            data[i] = getRowData(mRules.get(i));
        }

        TableModel tableModel = new DefaultTableModel(
                data, new Object[]{"ID*", "Brief description*", "Inspection*", "Fixes", "Highlight type", "Enabled"}
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Display the "Enable" column as checkbox
                if (columnIndex == 5) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 2 && column != 3;
            }
        };
        tableModel.addTableModelListener(new RulesTableModelListener(mRules));
        rulesTable.setModel(tableModel);

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
                    JPanel inspectionEditorForm = new InspectionEditorForm(mRules.get(row).getInspector()).getRootPanel();
                    populateEditor(inspectionEditorForm);
                } else if (col == 3) {
                    JPanel fixesEditorForm = new FixesEditorForm(mRules.get(row).getFixes()).getRootPanel();
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

    private Object[] getRowData(UastRule rule) {
        return new Object[]{rule.getId(), rule.getBriefDescription(), "...", "...", rule.getHighlightType(), rule.getEnabled()};
    }
}
