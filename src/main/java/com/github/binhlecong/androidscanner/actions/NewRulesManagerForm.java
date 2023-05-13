package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.Config;
import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy;
import com.github.binhlecong.androidscanner.rules.Inspection;
import com.github.binhlecong.androidscanner.rules.Rule;
import com.github.binhlecong.androidscanner.rules.RuleFile;
import com.github.binhlecong.androidscanner.rules.RulesManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class NewRulesManagerForm extends DialogWrapper {
    private JComboBox<String> selectLangDropdown;
    private JLabel selectLangLabel;
    private JScrollPane rulesScrollView;
    private JTable rulesTable;
    private JButton addRuleButton;
    private JPanel rootPanel;
    private JPanel editorPanel;
    private JButton deleteRuleButton;

    private RuleFile mLanguageSelected = RuleFile.JAVA;
    private ArrayList<Rule> mRules = null;

    final private Project mProject;

    public NewRulesManagerForm(@Nullable Project project) {
        super(project);
        mProject = project;
        setTitle(Config.PLUGIN_NAME + " Manager Console");
        init();
        populateDialog();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return rootPanel;
    }

    @Override
    protected void doOKAction() {
        switch (mLanguageSelected) {
            case JAVA:
                RulesManager.INSTANCE.saveJavaRules(mRules);
                break;
            case KOTLIN:
                RulesManager.INSTANCE.saveKotlinRules(mRules);
                break;
            case XML:
                RulesManager.INSTANCE.saveXmlRules(mRules);
                break;
            default:
                break;
        }
        RulesManager.INSTANCE.updateRules(mLanguageSelected, mProject);
        super.doOKAction();
    }

    private void populateDialog() {
        populateDropdownList();
        populateTable(mLanguageSelected);
        populateAddButton();
        populateDeleteButton();
    }

    private void populateDeleteButton() {
        deleteRuleButton.addActionListener(actionEvent -> {
            DefaultTableModel tableModel = (DefaultTableModel) rulesTable.getModel();
            if (tableModel == null) {
                return;
            }

            int row = rulesTable.getSelectedRow();
            if (row == -1) {
                return;
            }

            mRules.remove(row);
            tableModel.removeRow(row);
        });
    }

    private void populateAddButton() {
        addRuleButton.addActionListener(event -> {
            DefaultTableModel tableModel = (DefaultTableModel) rulesTable.getModel();
            if (tableModel == null) {
                return;
            }

            Rule newRule = new Rule(
                    "",
                    "",
                    new Inspection("", new ArrayList<String>(0)),
                    new ArrayList<ReplaceStrategy>(),
                    "WARNING",
                    true);
            mRules.add(newRule);

            Object[] newRowData = getRowData(newRule);
            tableModel.insertRow(tableModel.getRowCount(), newRowData);
            rulesTable.changeSelection(tableModel.getRowCount() - 1, 0, false, false);

            JPanel inspectionEditorForm = new InspectionEditorForm(
                    new Inspection("", new ArrayList<String>())
            ).getRootPanel();
            populateEditor(inspectionEditorForm);
        });
    }

    private void populateDropdownList() {
        for (RuleFile option : RuleFile.values()) {
            selectLangDropdown.addItem(option.name());
        }
        selectLangDropdown.setSelectedIndex(0);
        selectLangDropdown.addActionListener(event -> {
            JComboBox<String> cb = (JComboBox<String>) event.getSource();
            Object selectedLangName = cb.getSelectedItem();
            if (selectedLangName == null) {
                return;
            }
            mLanguageSelected = RuleFile.valueOf((String) selectedLangName);
            populateTable(mLanguageSelected);
        });
    }

    private void populateTable(RuleFile language) {
        if (language == null) {
            mRules = RulesManager.INSTANCE.cloneJavaRules();
        } else {
            switch (language) {
                case JAVA:
                    mRules = RulesManager.INSTANCE.cloneJavaRules();
                    break;
                case KOTLIN:
                    mRules = RulesManager.INSTANCE.cloneKotlinRules();
                    break;
                case XML:
                    mRules = RulesManager.INSTANCE.cloneXmlRules();
                    break;
            }
        }
        Object[][] data = new Object[mRules.size()][];
        for (int i = 0; i < mRules.size(); i++) {
            data[i] = getRowData(mRules.get(i));
        }

        TableModel tableModel = new DefaultTableModel(
                data, new Object[]{"ID*", "Brief Description*", "Highlight Level", "Enabled", "Delete"}
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Display the "Enable" column as checkbox
                if (columnIndex == 3) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        tableModel.addTableModelListener(new RulesTableModelListener(mRules));
        rulesTable.setModel(tableModel);

        TableColumnModel columnsModel = rulesTable.getColumnModel();
        columnsModel.getColumn(0).setMinWidth(150);
        columnsModel.getColumn(1).setMinWidth(400);
        columnsModel.getColumn(2).setMaxWidth(100);
        columnsModel.getColumn(3).setMaxWidth(100);
        columnsModel.getColumn(4).setMaxWidth(100);
        columnsModel.getColumn(4).setCellEditor(new ButtonCell());
        // ImageIcon icon = new ImageIcon(NewRulesManagerForm.class.getResource('/src/main/resources/icons/trash-filled.png'))

        rulesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                int row = rulesTable.rowAtPoint(event.getPoint());
                int col = rulesTable.columnAtPoint(event.getPoint());
                if (row < 0 || col < 0) return;
                if (col >= 0 && col < 3) {
                    RuleDetailForm dialog = new RuleDetailForm(mRules.get(row), mRules, rulesTable, rulesTable.getSelectedRow());
                    dialog.setLocationRelativeTo(new javax.swing.JFrame());
                    dialog.setVisible(true);
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

    private Object[] getRowData(Rule rule) {
        return new Object[]{rule.getId(), rule.getBriefDescription(), rule.getHighlightType(), rule.getEnabled()};
    }
}
