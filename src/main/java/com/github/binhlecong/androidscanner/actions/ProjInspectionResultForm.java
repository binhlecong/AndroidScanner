package com.github.binhlecong.androidscanner.actions;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

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
            StringBuilder builder = new StringBuilder();
            PsiFile psiFile = fileAndIssues.getFile();
            ProblemDescriptor[] issues = fileAndIssues.getIssues();
            builder.append("<html>");
            builder.append(psiFile.getName())
                    .append(" <span style=\"color:#7a7a7a;\">")
                    .append(psiFile.getContainingDirectory())
                    .append("<i> ").append(issues.length).append(issues.length == 1 ? " problem" : " problems").append("</i>")
                    .append("</span>")
                    .append("<ul>");
            for (ProblemDescriptor issue : issues) {
                builder.append("<li>").append(issue.getDescriptionTemplate())
                        .append("<i style=\"color:#7a7a7a;\"> line ")
                        .append(issue.getLineNumber()).append("</i></li>");
            }
            builder.append("</ul>");
            builder.append("</html>");
            JLabel issueLabel = new JLabel(builder.toString());
            issueLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    FileEditorManager.getInstance(project).openFile(psiFile.getVirtualFile(), true);
                }
            });
            issuesPanel.add(issueLabel);
        }
    }
}
