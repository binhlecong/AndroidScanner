package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.rules.Rule;
import com.github.binhlecong.androidscanner.rules.RuleFile;
import com.github.binhlecong.androidscanner.rules.RulesManager;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static icons.MyIcons.DeleteIcon;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class RulesTableForm extends JPanel {
    private JScrollPane rulesScrollView;
    private JTable rulesTable;
    private JPanel rootPanel;
    private JButton addRuleButton;
    private JLabel countRuleLabel;

    private RuleFile mLanguageSelected = RuleFile.JAVA;

    private ArrayList<Rule> mRules = null;

    final private Project mProject;

    public RulesTableForm(Project project, RuleFile language){
        this.mProject = project;
        this.mLanguageSelected = language;
        populateTable();
        populateAddButton();
    }

    private void populateTable() {
        if (mLanguageSelected == null) {
            mRules = RulesManager.INSTANCE.cloneJavaRules();
        } else {
            switch (mLanguageSelected) {
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
        if (mRules.size() == 1)
            countRuleLabel.setText(mRules.size() + " rule found");
        else countRuleLabel.setText(mRules.size() + " rules found");
        Object[][] data = new Object[mRules.size()][];
        for (int i = 0; i < mRules.size(); i++) {
            data[i] = getRowData(mRules.get(i));
        }

        TableModel tableModel = new DefaultTableModel(
                data, new Object[]{"ID*", "Brief Description*", "Highlight Level", "Enabled", ""}
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Display the "Enable" column as checkbox
                if (columnIndex == 3) {
                    return Boolean.class;
                }
                if (columnIndex == 4) {
                    return ImageIcon.class;
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

        rulesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                int row = rulesTable.rowAtPoint(event.getPoint());
                int col = rulesTable.columnAtPoint(event.getPoint());
                if (row < 0 || col < 0) return;
                if (col >= 0 && col < 3) {
                    RuleDetailForm dialog = new RuleDetailForm(mProject, mRules.get(row), mLanguageSelected, mRules, rulesTable, rulesTable.getSelectedRow(), countRuleLabel);
                    dialog.setLocationRelativeTo(new javax.swing.JFrame());
                    dialog.setVisible(true);
                }
                if (col == 4) {
                    int confirmMessage = JOptionPane.showInternalConfirmDialog(null, "Do you want to delete this rule?", "Confirm delete rule", YES_NO_OPTION, QUESTION_MESSAGE);
                    if (confirmMessage == 0) {
                        DefaultTableModel tableModel = (DefaultTableModel) rulesTable.getModel();
                        if (tableModel == null) {
                            return;
                        }

                        if (row == -1) {
                            return;
                        }

                        mRules.remove(row);
                        tableModel.removeRow(row);
                        if (mRules.size() == 1)
                            countRuleLabel.setText(mRules.size() + " rule found");
                        else countRuleLabel.setText(mRules.size() + " rules found");
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
                    }
                }
            }
        });
    }

    private void populateAddButton() {
        addRuleButton.addActionListener(event -> {
            RuleDetailForm dialog = new RuleDetailForm(mProject, mLanguageSelected, mRules, rulesTable, countRuleLabel);
            dialog.setLocationRelativeTo(new javax.swing.JFrame());
            dialog.setVisible(true);
        });
    }
    private Object[] getRowData(Rule rule) {
        return new Object[]{rule.getId(), rule.getBriefDescription(), rule.getHighlightType(), rule.getEnabled(), DeleteIcon};
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
