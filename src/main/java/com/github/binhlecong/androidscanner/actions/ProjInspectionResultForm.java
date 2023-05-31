package com.github.binhlecong.androidscanner.actions;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ProjInspectionResultForm extends JPanel {
    private JPanel rootPanel;
    private JLabel inspectionDetailLabel;
    private JPanel issuesPanel;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public ProjInspectionResultForm(ArrayList<ProjectInspectionAction.FileAndIssues> inspectionResult, Project project) {
        super();
        BoxLayout boxLayout = new BoxLayout(issuesPanel, BoxLayout.Y_AXIS);
        issuesPanel.setLayout(boxLayout);

        for (ProjectInspectionAction.FileAndIssues fileAndIssues : inspectionResult) {
            PsiFile psiFile = fileAndIssues.getFile();
            ProblemDescriptor[] issues = fileAndIssues.getIssues();

            String fileBuilder = "<html>" + psiFile.getName() + "<span style=\"color:#7a7a7a;\">" +
                    psiFile.getContainingDirectory() + "<i> " + issues.length +
                    (issues.length == 1 ? " problem" : " problems") + "</i>" +
                    "</span><html>";
            JLabel fileLabel = new JLabel(fileBuilder);
            fileLabel.setBorder(JBUI.Borders.empty(5, 10, 2, 0));
            fileLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    FileEditorManager.getInstance(project).openFile(psiFile.getVirtualFile(), true);
                }
            });
            issuesPanel.add(fileLabel);

            for (ProblemDescriptor issue : issues) {
                String issueBuilder = "<html>&emsp;" + issue.getDescriptionTemplate() +
                        "<i style=\"color:#7a7a7a;\"> line " +
                        (issue.getLineNumber() + 1) + "</i></html>";
                JLabel issueLabel = new JLabel(issueBuilder);
                issueLabel.setBorder(JBUI.Borders.empty(2, 10, 2, 0));
                issueLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        FileEditorManager.getInstance(project).openEditor(
                                new OpenFileDescriptor(project,
                                        psiFile.getVirtualFile(),
                                        issue.getTextRangeInElement().getStartOffset()),
                                false);
                    }
                });
                issuesPanel.add(issueLabel);
            }
        }
    }
}
