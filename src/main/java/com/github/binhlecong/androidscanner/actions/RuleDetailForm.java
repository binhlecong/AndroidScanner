package com.github.binhlecong.androidscanner.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import com.github.binhlecong.androidscanner.rules.Rule;

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

    private ArrayList<Rule> mRules;

    public RuleDetailForm(Rule rule, ArrayList<Rule> rules, JTable rulesTable, int index) {
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
        JPanel inspectionEditorForm = new InspectionEditorForm(rule.getInspector()).getRootPanel();
        populateInspectionEditor(inspectionEditorForm);
        JPanel fixesEditorForm = new FixesEditorForm(rule.getFixes()).getRootPanel();
        populateFixesEditor(fixesEditorForm);
        this.mRules = rules;

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
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

    private void onOK() {
        // add your code here
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


    /* public static void main(String[] args) {
        RuleDetailForm dialog = new RuleDetailForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    } */
}
