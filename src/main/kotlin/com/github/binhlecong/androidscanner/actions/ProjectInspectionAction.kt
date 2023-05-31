package com.github.binhlecong.androidscanner.actions

import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.inspections.UastInspection
import com.github.binhlecong.androidscanner.inspections.XmlInspection
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import java.io.File
import java.util.*
import javax.swing.JOptionPane
import javax.swing.JPanel
import kotlin.streams.toList


class ProjectInspectionAction : AnAction() {

    class FileAndIssues(val file: PsiFile, val issues: Array<ProblemDescriptor>)

    override fun actionPerformed(e: AnActionEvent) {
        if (e.project == null) return
        try {
            val project = e.project!!
            ProjectInspectionForm(project, ProjectInspectionForm.ProjectInspector { scopeType: Int, fileTypes ->
                var inspectionResult: ArrayList<FileAndIssues>? = null
                when (scopeType) {
                    0 -> {
                        val basePath = project.basePath!!
                        val allFiles = findFilesInDirAndSubdir(project, File(basePath), fileTypes)
                        inspectionResult = inspectFiles(allFiles, project)
                    }

                    1 -> {
                        val basePath = project.basePath + "/app/src/main"
                        val allFiles = findFilesInDirAndSubdir(project, File(basePath), fileTypes)
                        inspectionResult = inspectFiles(allFiles, project)
                    }

                    2 -> {
                        val basePath = project.basePath + "/app/src/test"
                        val allFiles = findFilesInDirAndSubdir(project, File(basePath), fileTypes)
                        inspectionResult = inspectFiles(allFiles, project)
                    }

                    3 -> {
                        val openedFiles = findOpenedEditorAsPsiFiles(project, fileTypes)
                        inspectionResult = inspectFiles(openedFiles, project)
                    }
                }

                if (inspectionResult == null) {
                    throw RuntimeException("contentManager is null")
                }

                JOptionPane.showMessageDialog(null, "Done", Config.PLUGIN_NAME, JOptionPane.INFORMATION_MESSAGE)
                val contentManager =
                    ToolWindowManager.getInstance(project).getToolWindow(Config.PLUGIN_NAME)?.contentManager
                if (contentManager != null) {
                    val content = contentManager.factory.createContent(
                        createResultPanel(inspectionResult, project),
                        getDisplayName(scopeType, project.name),
                        false,
                    )
                    //content.isCloseable = true
                    contentManager.addContent(content)
                } else {
                    throw RuntimeException("contentManager is null")
                }
            }).show()
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(null, e.message, Config.PLUGIN_NAME, JOptionPane.ERROR_MESSAGE)
            throw RuntimeException(e)
        }
    }

    private fun inspectFiles(psiFiles: List<PsiFile?>, project: Project): ArrayList<FileAndIssues> {
        val inspectionResult = ArrayList<FileAndIssues>()
        for (psiFile in psiFiles) {
            var issues: Array<ProblemDescriptor>? = null
            if (psiFile == null) continue
            val inspectionManager = InspectionManager.getInstance(project)
            when (psiFile.javaClass.simpleName) {
                "XmlFileImpl" -> {
                    val xmlInspection = XmlInspection()
                    issues = xmlInspection.checkFile(psiFile, inspectionManager, false)
                }

                "PsiJavaFileImpl" -> {
                    val uastInspection = UastInspection()
                    issues = uastInspection.checkFile(psiFile, inspectionManager, false)
                }
            }
            if (issues.isNullOrEmpty()) continue
            inspectionResult.add(FileAndIssues(psiFile, issues))
        }
        return inspectionResult
    }

    private fun getDisplayName(scopeType: Int, projectName: String): String {
        val nameBuilder = StringBuilder()
        when (scopeType) {
            0 -> nameBuilder.append("Full project")
            1 -> nameBuilder.append("Source files")
            2 -> nameBuilder.append("Test files")
            3 -> nameBuilder.append("Opening files")
        }
        nameBuilder.append(" scan on '$projectName'")
        return nameBuilder.toString()
    }

    private fun createResultPanel(fileNIssues: ArrayList<FileAndIssues>, project: Project): JPanel {
        return ProjInspectionResultForm(fileNIssues, project).rootPanel
    }

    private fun findFilesInDirAndSubdir(project: Project, folder: File, extensions: ArrayList<String>): List<PsiFile?> {
        val psiFiles = ArrayList<PsiFile?>()
        val fileStack = Stack<File>()
        fileStack.add(folder)
        while (!fileStack.isEmpty()) {
            val file = fileStack.pop()
            if (file.isDirectory) {
                if (file.listFiles() != null) {
                    file.listFiles()?.let { Collections.addAll(fileStack, *it) }
                }
            } else {
                val fileName = file.name
                for (extension in extensions) {
                    if (fileName.endsWith(extension)) {
                        val virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://" + file.absolutePath)
                            ?: continue
                        val psiFile = PsiManager.getInstance(project).findFile(virtualFile) ?: continue
                        psiFiles.add(psiFile)
                        break
                    }
                }
            }
        }
        return psiFiles.toList()
    }

    private fun findOpenedEditorAsPsiFiles(project: Project, fileTypes: ArrayList<String>): List<PsiFile?> {
        val editorManager = FileEditorManager.getInstance(project)
        return Arrays.stream(editorManager.openFiles)
            .map { file: VirtualFile? -> PsiManager.getInstance(project).findFile(file!!) }
            .filter(Objects::nonNull).filter {
                isSelectedFileType(it, fileTypes)
            }.toList()
    }

    private fun isSelectedFileType(psiFile: PsiFile?, fileTypes: ArrayList<String>): Boolean {
        if (psiFile == null) return false
        for (fileType in fileTypes) {
            if (psiFile.name.endsWith(fileType)) return true
        }
        return false
    }
}