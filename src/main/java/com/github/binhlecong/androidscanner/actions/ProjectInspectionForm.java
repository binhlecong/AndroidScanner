package com.github.binhlecong.androidscanner.actions;

import com.github.binhlecong.androidscanner.Config;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
//        PsiManager psiManager = PsiManager.getInstance(mProject);
//        Collection<VirtualFile> virtualFiles = FilenameIndex.getAllFilesByExt(mProject, "java", GlobalSearchScope.);
//        List<PsiFile> psiFiles = PsiUtilCore.toPsiFiles(psiManager, virtualFiles);
//        JOptionPane.showMessageDialog(null, "java files: " + mProject.getBasePath() + " " + psiFiles.size() + " " + virtualFiles.size(), "test", JOptionPane.INFORMATION_MESSAGE);
        try {
            writeStringToFile(projectPath, logFile);
            writeStringToFile("\n\n", logFile);
            visitFilesForFolder(new File(projectPath));
            writeStringToFile("\nend", logFile);
            JOptionPane.showMessageDialog(null, "done", projectPath, JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void visitFilesForFolder(final File folder) {
        try {
            writeStringToFile(folder.getAbsolutePath(), logFile);
            writeStringToFile("  >>>  ", logFile);
            writeStringToFile(Integer.toString(folder.listFiles().length), logFile);
            writeStringToFile("  >>>  ", logFile);
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    visitFilesForFolder(fileEntry);
                } else {
                    String fileName = fileEntry.getName();
                    if (fileName.endsWith(".java") || fileName.endsWith(".kt") || fileName.endsWith(".xml")) {
                        try {
                            writeStringToFile("  --  ", logFile);
                            writeStringToFile(fileEntry.getAbsolutePath(), logFile);
                            writeStringToFile("  >>  ", logFile);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }



//                        Collection<VirtualFile> virtualFiles = FilenameIndex.getVirtualFilesByName(
//                                fileEntry.getAbsolutePath(),
//                                GlobalSearchScope.allScope(mProject));
//                        PsiManager psiManager = PsiManager.getInstance(mProject);
//                        List<PsiFile> psiFiles = PsiUtilCore.toPsiFiles(psiManager, virtualFiles);


                        try {
//                            writeStringToFile(Integer.toString(virtualFiles.size()), logFile);
//                            writeStringToFile(" >>> ", logFile);
//                            writeStringToFile(Integer.toString(psiFiles.size()), logFile);
//                            writeStringToFile("\n", logFile);
                            VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://" + fileEntry.getAbsolutePath());
                            if (virtualFile == null) {
                                writeStringToFile("non vir", logFile);
                                continue;
                            } else {
                                writeStringToFile("has vir", logFile);
                            }
                            writeStringToFile("  --  ", logFile);
                            PsiFile psiFile = PsiManager.getInstance(mProject).findFile(virtualFile);
                            if (psiFile == null) {
                                writeStringToFile("non psi", logFile);
                            } else {
                                writeStringToFile("has psi", logFile);
                            }
                            writeStringToFile("\n", logFile);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeStringToFile(String str, String fileName) throws IOException {
        Writer output;
        output = new BufferedWriter(new FileWriter(fileName, true));
        output.append(str);
        output.close();
    }
}
