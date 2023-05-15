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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class ProjectInspectionForm extends DialogWrapper {
    private JPanel rootPanel;
    private JComboBox<String> scopeOptionsComboBox;
    private JCheckBox javaCheckBox;
    private JCheckBox ktCheckBox;
    private JCheckBox xmlCheckBox;
    private final String[] scopeOptions = {"All", "Project Source Files", "Project Test Files", "Opening Files"};
    final private Project mProject;

    final private String logFile = "E:\\armordroid_report.txt";

    public ProjectInspectionForm(@Nullable Project project) {
        super(project);
        mProject = project;
        setTitle(Config.PLUGIN_NAME + " Inspect Project");
        init();
        populateDialog();
    }

    private void populateDialog() {
        for (String option : scopeOptions) {
            scopeOptionsComboBox.addItem(option);
        }
        scopeOptionsComboBox.setSelectedIndex(0);

        javaCheckBox.setSelected(true);
        ktCheckBox.setSelected(true);
        xmlCheckBox.setSelected(true);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return rootPanel;
    }

    @Override
    protected void doOKAction() {
        ArrayList<String> fileExt = new ArrayList<>();
        if (javaCheckBox.isSelected()) fileExt.add(".java");
        if (ktCheckBox.isSelected()) fileExt.add(".kt");
        if (xmlCheckBox.isSelected()) fileExt.add(".xml");
        if (!fileExt.isEmpty()) {
            int selectedIndex = scopeOptionsComboBox.getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    inspectProject(mProject.getBasePath(), fileExt);
                    break;
                case 1:
                    String sourcePath = mProject.getBasePath() + "/app/src/main";
                    inspectProject(sourcePath, fileExt);
                    break;
                case 2:
                    String testPath = mProject.getBasePath() + "/app/src/test";
                    inspectProject(testPath, fileExt);
                    break;
                case 3:
                    inspectProject(mProject.getBasePath(), fileExt);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Fail to select custom scope", Config.PLUGIN_NAME, JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(null, "At least one file type must be selected", Config.PLUGIN_NAME, JOptionPane.WARNING_MESSAGE);
            return;
        }
        super.doOKAction();
    }

    private void inspectProject(String basePath, ArrayList<String> extensions) {
        if (basePath == null || basePath.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Fail to access project folder", Config.PLUGIN_NAME, JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            new FileWriter(logFile, false).close();
            Writer output = new BufferedWriter(new FileWriter(logFile, true));
            output.append("Location:\n");
            output.append(" ").append(basePath).append("\n");
            for (String extension : extensions)
                output.append(extension).append(" ");
            output.append("\n\n");
            visitFiles(new File(basePath), extensions, output);
            output.close();
            JOptionPane.showMessageDialog(null, "Done", Config.PLUGIN_NAME, JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), Config.PLUGIN_NAME, JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }


    private void visitFiles(final File folder, ArrayList<String> extensions, Writer output) {
        Stack<File> fileStack = new Stack<>();
        fileStack.add(folder);
        while (!fileStack.isEmpty()) {
            File file = fileStack.pop();
            if (file.isDirectory()) {
                Collections.addAll(fileStack, file.listFiles());
            } else {
                String fileName = file.getName();
                for (String extension : extensions) {
                    if (fileName.endsWith(extension)) {
                        try {
                            output.append("- ");
                            output.append(file.getAbsolutePath());
                            output.append("\n");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        try {
                            VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://" + file.getAbsolutePath());
                            if (virtualFile == null) {
                                continue;
                            }

                            PsiFile psiFile = PsiManager.getInstance(mProject).findFile(virtualFile);
                            if (psiFile == null) {
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

                            for (ProblemDescriptor issue : issues) {
                                output.append("  + ");
                                output.append(issue.getDescriptionTemplate());
                                output.append("\n");
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }
            }
        }
    }
}

//    private void visitFilesForFolder(final File folder, ArrayList<String> extensions, Writer output) {
//        for (final File fileEntry : folder.listFiles()) {
//            if (fileEntry.isDirectory()) {
//                visitFilesForFolder(fileEntry, extensions, output);
//            } else {
//                String fileName = fileEntry.getName();
//                for (String extension : extensions) {
//                    if (fileName.endsWith(extension)) {
//                        try {
//                            output.append("- ");
//                            output.append(fileEntry.getAbsolutePath());
//                            output.append("\n");
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//
//                        try {
//                            VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://" + fileEntry.getAbsolutePath());
//                            if (virtualFile == null) {
//                                continue;
//                            }
//
//                            PsiFile psiFile = PsiManager.getInstance(mProject).findFile(virtualFile);
//                            if (psiFile == null) {
//                                continue;
//                            }
//
//                            ProblemDescriptor[] issues = null;
//                            switch (psiFile.getClass().getSimpleName()) {
//                                case "XmlFileImpl":
//                                    XmlInspection xmlInspection = new XmlInspection();
//                                    issues = xmlInspection.checkFile(psiFile, InspectionManager.getInstance(mProject), false);
//                                    break;
//                                case "PsiJavaFileImpl":
//                                    UastInspection uastInspection = new UastInspection();
//                                    issues = uastInspection.checkFile(psiFile, InspectionManager.getInstance(mProject), false);
//                                    break;
//                            }
//
//                            for (ProblemDescriptor issue : issues) {
//                                output.append("  + ");
//                                output.append(issue.getDescriptionTemplate());
//                                output.append("\n");
//                            }
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                        break;
//                    }
//                }
//            }
//        }
//    }
