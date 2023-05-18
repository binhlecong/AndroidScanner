package com.github.binhlecong.androidscanner.actions;

import javax.swing.*;

public class RuleDetailFormValidator extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {

        if (input instanceof JTextField) {
            String text = ((JTextField) input).getText().trim();
            if (text.isEmpty())
                return false;
            if (text.equals(""))
                return false;
        } else if (input instanceof JComboBox) {
            String text = ((JComboBox) input).getSelectedItem().toString();
            if (text.isEmpty())
                return false;
            if (text.equals(""))
                return false;
        }
        return true;
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        boolean valid = verify(input);
        if (valid) {
            JOptionPane.showMessageDialog(null, "Invalid data");
        }

        return valid;
    }

    @Override
    public boolean shouldYieldFocus(JComponent source, JComponent input) {
        boolean valid = verify(input);
        if (valid) {
            JOptionPane.showMessageDialog(null, "Invalid data");
        }

        return valid;
    }
}