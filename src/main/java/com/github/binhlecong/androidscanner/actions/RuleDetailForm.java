package com.github.binhlecong.androidscanner.actions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy;
import com.github.binhlecong.androidscanner.rules.Inspection;
import com.github.binhlecong.androidscanner.rules.Rule;
import com.github.binhlecong.androidscanner.rules.RuleFile;
import com.github.binhlecong.androidscanner.rules.RulesManager;
import com.intellij.openapi.project.Project;
import org.gradle.internal.impldep.org.intellij.lang.annotations.Language;

import static icons.MyIcons.DeleteIcon;
import static javax.swing.JOptionPane.*;

public class RuleDetailForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField idTextField;
    private JTextField briefDescTextField;
    private JCheckBox enabledCheckbox;
    private JPanel inspectionEditorPanel;
    private JComboBox highlightTypeComboBox;
    private JPanel fixesEditorPanel;
    private JButton deleteRuleButton;
    private JLabel languageLabel;
    private ArrayList<Rule> mRules;
    private Rule mRule;

    private RuleFile mLanguage;
    final private Project mProject;

    public RuleDetailForm(Project project, Rule rule, RuleFile language, ArrayList<Rule> rules, JTable rulesTable, int index) {
        setContentPane(contentPane);
        setModal(true);
        setSize(1000, 700);
        getRootPane().setDefaultButton(buttonOK);
        setTitle(rule.getId() != "" ? rule.getId() : "Rule details");
        this.mLanguage = language;
        languageLabel.setText(this.mLanguage.toString() + " RULE");
        this.mRule = new Rule(rule.getId(), rule.getBriefDescription(), rule.getInspector(), rule.getFixes(), rule.getHighlightType(), rule.getEnabled());
        idTextField.setText(this.mRule.getId());
        briefDescTextField.setText(this.mRule.getBriefDescription());
        enabledCheckbox.setSelected(this.mRule.getEnabled());
        highlightTypeComboBox.getModel().setSelectedItem(this.mRule.getHighlightType());
        JPanel inspectionEditorForm = new InspectionEditorForm(this.mRule.getInspector()).getRootPanel();
        populateInspectionEditor(inspectionEditorForm);
        JPanel fixesEditorForm = new FixesEditorForm(this.mRule.getFixes()).getRootPanel();
        populateFixesEditor(fixesEditorForm);
        populateDeleteButton(language, rulesTable);
        this.mRules = rules;
        this.mProject = project;

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean validId = verifyInput(idTextField);
                if (validId == false){
                    JOptionPane.showMessageDialog(null, "Invalid ID");
                } else {
                    boolean validDesc = verifyInput(briefDescTextField);
                    if (validDesc == false){
                        JOptionPane.showMessageDialog(null, "Invalid brief description");
                    } else {
                        int confirmMessage = JOptionPane.showInternalConfirmDialog(null, "Do you want to save changes?", "Confirm edit rule", OK_CANCEL_OPTION, QUESTION_MESSAGE);
                        if (confirmMessage == 0) onOKEditRule(rulesTable, index);
                    }
                }

            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmMessage = JOptionPane.showInternalConfirmDialog(null, "Do you want to cancel?", "Cancel edit rule", OK_CANCEL_OPTION, QUESTION_MESSAGE);
                if (confirmMessage == 0) onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
    public RuleDetailForm(Project project, RuleFile language, ArrayList<Rule> rules, JTable rulesTable) {
        setContentPane(contentPane);
        setModal(true);
        setSize(1000, 700);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Rule details");
        this.mLanguage = language;
        languageLabel.setText(this.mLanguage.toString() + " RULE");
        this.mRule = new Rule(
                "",
                "",
                new Inspection("", new ArrayList<String>(0)),
                new ArrayList<ReplaceStrategy>(),
                "WARNING",
                true);
        idTextField.setText(this.mRule.getId());
        briefDescTextField.setText(this.mRule.getBriefDescription());
        enabledCheckbox.setSelected(this.mRule.getEnabled());
        highlightTypeComboBox.getModel().setSelectedItem(this.mRule.getHighlightType());
        JPanel inspectionEditorForm = new InspectionEditorForm(this.mRule.getInspector()).getRootPanel();
        populateInspectionEditor(inspectionEditorForm);
        JPanel fixesEditorForm = new FixesEditorForm(this.mRule.getFixes()).getRootPanel();
        populateFixesEditor(fixesEditorForm);
        this.mRules = rules;
        this.mProject = project;

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean validId = verifyInput(idTextField);
                if (validId == false) {
                    JOptionPane.showMessageDialog(null, "Invalid ID");
                } else {
                    boolean validDesc = verifyInput(briefDescTextField);
                    if (validDesc == false) {
                        JOptionPane.showMessageDialog(null, "Invalid brief description");
                    } else onOKAddRule(rulesTable);
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmMessage = JOptionPane.showInternalConfirmDialog(null, "Do you want to cancel?", "Cancel edit rule", OK_CANCEL_OPTION, QUESTION_MESSAGE);
                if (confirmMessage == 0) onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOKEditRule(JTable rulesTable, int index) {
        // add your code here
        DefaultTableModel tableModel = (DefaultTableModel) rulesTable.getModel();
        if (tableModel == null) {
            return;
        }
        this.mRules.set(index, this.mRule);
        tableModel.setValueAt(idTextField.getText(),index,0);
        tableModel.setValueAt(briefDescTextField.getText(), index, 1);
        tableModel.setValueAt(highlightTypeComboBox.getSelectedItem().toString(), index, 2);
        tableModel.setValueAt(enabledCheckbox.getModel().isSelected(), index, 3);
        rulesTable.changeSelection(index, 0, false, false);
        switch (mLanguage) {
            case JAVA:
                RulesManager.INSTANCE.saveJavaRules(this.mRules);
                break;
            case KOTLIN:
                RulesManager.INSTANCE.saveKotlinRules(this.mRules);
                break;
            case XML:
                RulesManager.INSTANCE.saveXmlRules(this.mRules);
                break;
            default:
                break;
        }
        RulesManager.INSTANCE.updateRules(mLanguage, mProject);
        dispose();
    }

    private void onOKAddRule(JTable rulesTable) {
        // add your code here
        this.mRules.add(this.mRule);
        DefaultTableModel tableModel = (DefaultTableModel) rulesTable.getModel();
        if (tableModel == null) {
            return;
        }
        Object[] newRowData = getRowData(this.mRule);
        tableModel.insertRow(tableModel.getRowCount(), newRowData);
        rulesTable.changeSelection(tableModel.getRowCount() - 1, 0, false, false);
        switch (mLanguage) {
            case JAVA:
                RulesManager.INSTANCE.saveJavaRules(this.mRules);
                break;
            case KOTLIN:
                RulesManager.INSTANCE.saveKotlinRules(this.mRules);
                break;
            case XML:
                RulesManager.INSTANCE.saveXmlRules(this.mRules);
                break;
            default:
                break;
        }
        RulesManager.INSTANCE.updateRules(mLanguage, mProject);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void populateInspectionEditor(Component component) {
        inspectionEditorPanel.setLayout(new java.awt.BorderLayout());
        inspectionEditorPanel.removeAll();
        if (component != null) {
            inspectionEditorPanel.add(component);
        }
        inspectionEditorPanel.validate();
        inspectionEditorPanel.repaint();
    }
    private void populateFixesEditor(Component component){
        fixesEditorPanel.setLayout(new java.awt.BorderLayout());
        fixesEditorPanel.removeAll();
        if (component != null) {
            fixesEditorPanel.add(component);
        }
        fixesEditorPanel.validate();
        fixesEditorPanel.repaint();
    }

    private void populateDeleteButton(RuleFile language, JTable rulesTable) {
        deleteRuleButton.addActionListener(actionEvent -> {
            int confirmMessage = JOptionPane.showInternalConfirmDialog(null, "Do you want to delete this rule?", "Confirm delete rule", YES_NO_OPTION, QUESTION_MESSAGE);
            if (confirmMessage == 0) {
                DefaultTableModel tableModel = (DefaultTableModel) rulesTable.getModel();
                if (tableModel == null) {
                    return;
                }

                int row = rulesTable.getSelectedRow();
                if (row == -1) {
                    return;
                }
                this.mRules.remove(row);
                tableModel.removeRow(row);
                switch (language) {
                    case JAVA:
                        RulesManager.INSTANCE.saveJavaRules(this.mRules);
                        break;
                    case KOTLIN:
                        RulesManager.INSTANCE.saveKotlinRules(this.mRules);
                        break;
                    case XML:
                        RulesManager.INSTANCE.saveXmlRules(this.mRules);
                        break;
                    default:
                        break;
                }
                RulesManager.INSTANCE.updateRules(language, mProject);
                dispose();
            }
        });
    }

    private boolean verifyInput(JComponent input){
        if (input instanceof JTextField) {
            String text = ((JTextField) input).getText().trim();
            if (text.isEmpty())
                return false;
            if (text.equals(""))
                return false;
        }
        return true;
    }

    private boolean verifyUnique(JComponent input){

        return true;
    }
    private Object[] getRowData(Rule rule) {
        return new Object[]{rule.getId(), rule.getBriefDescription(), rule.getHighlightType(), rule.getEnabled(), DeleteIcon};
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }


    /* public static void main(String[] args) {
        RuleDetailForm dialog = new RuleDetailForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    } */
}
