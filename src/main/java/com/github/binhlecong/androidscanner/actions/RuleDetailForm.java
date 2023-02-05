package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy;
import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy;
import com.github.binhlecong.androidscanner.rules.UastRule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class RuleDetailForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField idTextField;
    private JTextField briefDescTextField;
    private JComboBox categoryComboBox;
    private JComboBox highlightTypeComboBox;
    private JCheckBox enabledCheckbox;
    private JPanel inspectorPane;
    private JPanel fixesPane;

    private ArrayList<UastRule> mRules;

    public RuleDetailForm(){

    }

    public RuleDetailForm(UastRule rule, ArrayList<UastRule> rules, JTable rulesTable, int index) {
        setContentPane(contentPane);
        setModal(true);
        setSize(1000, 500);
        getRootPane().setDefaultButton(buttonOK);
        setTitle(rule.getId() != "" ? rule.getId() : "Rule details");
        InputVerifier verifier = new RuleDetailFormValidator();
        idTextField.setInputVerifier(verifier);
        idTextField.setText(rule.getId());
        briefDescTextField.setText(rule.getBriefDescription());
        briefDescTextField.setInputVerifier(verifier);
        enabledCheckbox.setSelected(rule.getEnabled());
        highlightTypeComboBox.getModel().setSelectedItem(rule.getHighlightType());
        highlightTypeComboBox.setInputVerifier(verifier);
        /* JPanel inspectionEditorForm = new InspectionEditorForm(rule.getInspector()).getRootPanel();
        populateInspector(inspectionEditorForm);
        JPanel fixesEditorForm = new FixesEditorForm(rule.getFixes()).getRootPanel();
        populateFixes(fixesEditorForm); */
        this.mRules = rules;

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK(rules, rulesTable,index);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

    public RuleDetailForm(ArrayList<UastRule> rules, JTable rulesTable) {
        setContentPane(contentPane);
        setModal(true);
        setSize(1000, 500);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Add Rule");
        this.mRules = rules;
        InputVerifier verifier = new RuleDetailFormValidator();
        idTextField.setInputVerifier(verifier);
        briefDescTextField.setInputVerifier(verifier);
        highlightTypeComboBox.setInputVerifier(verifier);
        /*JPanel inspectionEditorForm = new InspectionEditorForm(null).getRootPanel();
        populateInspector(inspectionEditorForm);
        JPanel fixesEditorForm = new FixesEditorForm(null).getRootPanel();
        populateFixes(fixesEditorForm);*/

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAddOK(rules, rulesTable);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

    private void onOK(ArrayList<UastRule> rules, JTable rulesTable, int index) {
        // add your code here
        DefaultTableModel tableModel = (DefaultTableModel) rulesTable.getModel();
        if (tableModel == null) {
            return;
        }
        UastRule newRule = new UastRule(
                idTextField.getText(),
                briefDescTextField.getText(),
                new UastInspectionStrategy("", new ArrayList<String>(0)),
                new ArrayList<ReplaceStrategy>(),
                highlightTypeComboBox.getSelectedItem().toString(),
                enabledCheckbox.getModel().isSelected());
        tableModel.setValueAt(idTextField.getText(),index,0);
        tableModel.setValueAt(briefDescTextField.getText(), index, 1);
        rulesTable.changeSelection(index, 0, false, false);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary

        dispose();
    }

    private void onAddOK(ArrayList<UastRule> rules, JTable rulesTable) {
        // add your code here
        DefaultTableModel tableModel = (DefaultTableModel) rulesTable.getModel();
                if (tableModel == null) {
                    return;
                }


        UastRule newRule = new UastRule(
                idTextField.getText(),
                briefDescTextField.getText(),
                new UastInspectionStrategy("", new ArrayList<String>(0)),
                new ArrayList<ReplaceStrategy>(),
                highlightTypeComboBox.getSelectedItem().toString(),
                enabledCheckbox.getModel().isSelected());
                mRules.add(newRule);

        Object[] newRowData = getRowData(newRule);
        tableModel.insertRow(tableModel.getRowCount(), newRowData);
        rulesTable.changeSelection(tableModel.getRowCount() - 1, 0, false, false);
        dispose();
    }
    private Object[] getRowData(UastRule rule) {
        return new Object[]{rule.getId(), rule.getBriefDescription(), rule.getHighlightType(), rule.getEnabled()};
    }

    private void populateInspector(Component component) {
        inspectorPane.setLayout(new java.awt.BorderLayout());
        inspectorPane.removeAll();
        if (component != null) {
            inspectorPane.add(component);
        }
        inspectorPane.validate();
        inspectorPane.repaint();
    }

    private void populateFixes(Component component) {
        fixesPane.setLayout(new java.awt.BorderLayout());
        fixesPane.removeAll();
        if (component != null) {
            fixesPane.add(component);
        }
        fixesPane.validate();
        fixesPane.repaint();
    }
}
