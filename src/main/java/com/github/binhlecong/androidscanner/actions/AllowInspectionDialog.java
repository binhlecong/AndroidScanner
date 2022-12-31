package com.github.binhlecong.androidscanner.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AllowInspectionDialog extends DialogWrapper {
    private JPanel rootPanel;

    public AllowInspectionDialog(@Nullable Project project) {
        super(project);
        setTitle("AndroidScanner plugin");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return rootPanel;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }
}
