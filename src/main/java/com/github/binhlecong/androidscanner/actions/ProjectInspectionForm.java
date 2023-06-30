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
    final private ProjectInspector mProjectInspector;

    final static public String logFile = Config.Companion.getPATH() + "/armordroid_report.txt";

    public interface ProjectInspector {
        void inspect(int scopeType, ArrayList<String> fileTypes);
    }

    public ProjectInspectionForm(@Nullable Project project, ProjectInspector projectInspector) {
        super(project);
        mProjectInspector = projectInspector;
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
            mProjectInspector.inspect(selectedIndex, fileExt);
        } else {
            JOptionPane.showMessageDialog(null, "At least one file type must be selected", Config.PLUGIN_NAME, JOptionPane.WARNING_MESSAGE);
            return;
        }
        super.doOKAction();
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
