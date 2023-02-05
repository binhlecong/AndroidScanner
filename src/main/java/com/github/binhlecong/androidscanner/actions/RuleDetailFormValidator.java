package com.github.binhlecong.androidscanner.actions;

import javax.swing.*;

public class RuleDetailFormValidator extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        String text = null;

        if (input instanceof JTextField) {
            text = ((JTextField) input).getText();
        } else if (input instanceof JComboBox) {
            text = ((JComboBox) input).getSelectedItem().toString();
        }

        if (text == null || text == "")
            return false;
        return true;
    }

    @Override
    public boolean shouldYieldFocus(JComponent input) {
        boolean valid = verify(input);
        if (!valid) {
            JOptionPane.showMessageDialog(null, "Invalid data");
        }

        return valid;
    }

    @Override
    public boolean shouldYieldFocus(JComponent source, JComponent input) {
        boolean valid = verify(input);
        if (!valid) {
            JOptionPane.showMessageDialog(null, "Invalid data");
        }

        return valid;
    }
}
