package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.rules.UastRule;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RuleDetailFormDialog extends DialogWrapper {

    private JTextField idTextField;
    private JTextField briefDescTextField;
    private JComboBox categoryComboBox;
    private JComboBox highlightTypeComboBox;

    private JCheckBox enabledCheckbox;
    private JPanel contentPane;
    private JPanel inspectorPane;
    private JPanel fixesPane;

    private ArrayList<UastRule> mRules;

    private boolean add;

    protected RuleDetailFormDialog(@NotNull Component parent, boolean canBeParent, UastRule rule, ArrayList<UastRule> rules) {
        super(parent, canBeParent);
        setTitle(rule.getId() != "" ? rule.getId() : "Rule details");
        init();
        idTextField.setText(rule.getId());
        briefDescTextField.setText(rule.getBriefDescription());
        enabledCheckbox.setSelected(rule.getEnabled());
        highlightTypeComboBox.getModel().setSelectedItem(rule.getHighlightType());
        this.mRules = rules;
        // categoryComboBox.getModel().setSelectedItem(rule.getCategory());
        /* JPanel inspectionEditorForm = new InspectionEditorForm(rule.getInspector()).getRootPanel();
        populateInspector(inspectionEditorForm);
         JPanel fixesEditorForm = new FixesEditorForm(rule.getFixes()).getRootPanel();
        populateFixes(fixesEditorForm); */
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


    @Override
    protected @Nullable JComponent createCenterPanel() {
        return contentPane;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
    }
}
