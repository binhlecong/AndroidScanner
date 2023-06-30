package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.Config;
import com.github.binhlecong.androidscanner.rules.Rule;
import com.github.binhlecong.androidscanner.rules.RuleFile;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class RulesManagerForm extends DialogWrapper {
    private JComboBox<String> selectLangDropdown;
    private JLabel selectLangLabel;
    private JPanel rootPanel;
    private JPanel ruleTablePanel;
    private RuleFile mLanguageSelected = RuleFile.JAVA;
    private ArrayList<Rule> mRules = null;

    final private Project mProject;

    public RulesManagerForm(@Nullable Project project) {
        super(project);
        mProject = project;
        setTitle(Config.PLUGIN_NAME + " Rule Manager Console");
        init();
        populateDialog();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return rootPanel;
    }

    private void populateDialog() {
        populateDropdownList();
        JPanel rulesTableForm = new RulesTableForm(mProject, mLanguageSelected).getRootPanel();
        populateRuleTable(rulesTableForm);
    }

    private void populateRuleTable(Component component) {
        ruleTablePanel.setLayout(new java.awt.BorderLayout());
        ruleTablePanel.removeAll();
        if (component != null) {
            ruleTablePanel.add(component);
        }
        ruleTablePanel.validate();
        ruleTablePanel.repaint();
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
            JPanel rulesTableForm = new RulesTableForm(mProject, mLanguageSelected).getRootPanel();
            populateRuleTable(rulesTableForm);
        });
    }

}
