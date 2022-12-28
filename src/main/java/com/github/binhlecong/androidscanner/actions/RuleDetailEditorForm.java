package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.rules.UastRule;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RuleDetailEditorForm extends JDialog {
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

    public RuleDetailEditorForm(@NotNull Component parent, boolean canBeParent, UastRule rule, ArrayList<UastRule> rules) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK(rules);
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

    private void onOK(ArrayList<UastRule> rules) {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
