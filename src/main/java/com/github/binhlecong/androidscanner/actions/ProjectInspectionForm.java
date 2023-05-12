package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.Config;
import com.github.binhlecong.androidscanner.inspections.UastInspection;
import com.github.binhlecong.androidscanner.inspections.XmlInspection;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.*;

public class ProjectInspectionForm extends DialogWrapper {
    private JPanel rootPanel;
    private JRadioButton wholeProjectRadioButton;
    private JRadioButton customScopeRadioButton;

    final private Project mProject;

    final private String logFile = "E:\\log.txt";

    public ProjectInspectionForm(@Nullable Project project) {
        super(project);
        mProject = project;
        setTitle(Config.PLUGIN_NAME + " Inspect Project");
        init();
        populateDialog();
    }

    private void populateDialog() {
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(wholeProjectRadioButton);
        buttonGroup.add(customScopeRadioButton);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return rootPanel;
    }

    @Override
    protected void doOKAction() {
        inspectProject();
        super.doOKAction();
    }

    private void inspectProject() {
        String projectPath = mProject.getBasePath();
        if (projectPath == null || projectPath.isEmpty()) {
            return;
        }

        try {
            new FileWriter(logFile, false).close();

            writeStringToFile(projectPath, logFile);
            writeStringToFile("\n\n", logFile);
            visitFilesForFolder(new File(projectPath));
            writeStringToFile("\n-- END --", logFile);
            JOptionPane.showMessageDialog(null, "done", projectPath, JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void visitFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                visitFilesForFolder(fileEntry);
            } else {
                String fileName = fileEntry.getName();
                if (fileName.endsWith(".java") || fileName.endsWith(".kt") || fileName.endsWith(".xml")) {
                    try {
                        writeStringToFile(" - ", logFile);
                        writeStringToFile(fileEntry.getAbsolutePath(), logFile);
                        writeStringToFile("\n", logFile);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://" + fileEntry.getAbsolutePath());
                        //writeStringToFile("    - ", logFile);
                        if (virtualFile == null) {
                            //writeStringToFile("non vir", logFile);
                            continue;
                        }
//                        else {
//                            writeStringToFile("has vir", logFile);
//                        }
                        //writeStringToFile("  /  ", logFile);
                        PsiFile psiFile = PsiManager.getInstance(mProject).findFile(virtualFile);
                        if (psiFile == null) {
                            //writeStringToFile("non psi", logFile);
                            continue;
                        }

                        ProblemDescriptor[] issues = null;
                        switch (psiFile.getClass().getSimpleName()) {
                            case "XmlFileImpl":
                                XmlInspection xmlInspection = new XmlInspection();
                                issues = xmlInspection.checkFile(psiFile, InspectionManager.getInstance(mProject), false);
                                break;
                            case "PsiJavaFileImpl":
                                UastInspection uastInspection = new UastInspection();
                                issues = uastInspection.checkFile(psiFile, InspectionManager.getInstance(mProject), false);
                                break;
                        }
                        writeStringToFile("    + ", logFile);
                        writeStringToFile(issues != null ? Integer.toString(issues.length) : "none issues", logFile);
                        writeStringToFile("\n", logFile);

//                        else {
//                            writeStringToFile("has psi", logFile);
//                        }
//                        writeStringToFile("\n", logFile);
//
//                        writeStringToFile("    - ", logFile);
//                        writeStringToFile(psiFile.getClass().getSimpleName(), logFile);
//                        writeStringToFile("\n", logFile);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void writeStringToFile(String str, String fileName) throws IOException {
        Writer output;
        output = new BufferedWriter(new FileWriter(fileName, true));
        output.append(str);
        output.close();
    }
}
