package com.github.binhlecong.androidscanner.actions;

import javax.swing.*;

public class ProjInspectionResultForm extends JPanel {
    private JPanel rootPanel;
    private JLabel inspectionDetailLabel;
    private JLabel resultLabel;

    public JPanel getRootPanel() {
        return rootPanel;
    }
    public ProjInspectionResultForm(String result) {
        super();
        resultLabel.setText(result);
    }
}
