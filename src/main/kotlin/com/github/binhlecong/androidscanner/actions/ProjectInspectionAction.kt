package com.github.binhlecong.androidscanner.actions

import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.inspections.UastInspection
import com.github.binhlecong.androidscanner.inspections.XmlInspection
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiManager
import java.io.File
import java.io.FileWriter
import java.util.*
import javax.swing.JOptionPane
import javax.swing.JPanel

class ProjectInspectionAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        if (e.project == null) return
        val project = e.project!!
        ProjectInspectionForm(project, ProjectInspectionForm.ProjectInspector { scopeType: Int, fileTypes ->
            var basePath: String? = null
            when (scopeType) {
                0 -> basePath = project.getBasePath()!!
                1 -> basePath = project.getBasePath() + "/app/src/main"
                2 -> basePath = project.getBasePath() + "/app/src/test"
                3 -> basePath = project.getBasePath()!!
            }

            if (basePath.isNullOrEmpty()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Fail to access project folder",
                    Config.PLUGIN_NAME,
                    JOptionPane.ERROR_MESSAGE
                )
            } else {
                try {
                    val resultString: String = inspectProject(project, basePath, fileTypes)
                    val contentManager =
                        ToolWindowManager.getInstance(project).getToolWindow("ArmorDroid")?.contentManager
                    if (contentManager != null) {
                        val content = contentManager.factory.createContent(
                            createResultPanel(resultString),
                            getDisplayName(scopeType, project.name),
                            false,
                        )
                        contentManager.addContent(content)
                    } else {
                        throw RuntimeException("contentManager is null")
                    }
                } catch (e: java.lang.Exception) {
                    JOptionPane.showMessageDialog(null, e.message, Config.PLUGIN_NAME, JOptionPane.ERROR_MESSAGE)
                    throw RuntimeException(e)
                }
            }
        }).show()
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

    private fun createResultPanel(result: String): JPanel {
        return ProjInspectionResultForm(result).rootPanel
    }

    private fun inspectProject(project: Project, basePath: String, extensions: ArrayList<String>): String {
        try {
            FileWriter(ProjectInspectionForm.logFile, false).close()
            val output: StringBuilder = StringBuilder()
            output.append("<html><b>Location:</b> $basePath")
            output.append("<p>Selected file types:<ul>")
            for (extension in extensions) output.append("<li>$extension</li>")
            output.append("</ul></p>")

            visitFiles(project, File(basePath), extensions, output)
            output.append("</html>")
            JOptionPane.showMessageDialog(null, "Done", Config.PLUGIN_NAME, JOptionPane.INFORMATION_MESSAGE)
            return output.toString()
        } catch (e: java.lang.Exception) {
            throw RuntimeException(e)
        }
    }

    private fun visitFiles(project: Project, folder: File, extensions: ArrayList<String>, output: StringBuilder) {
        val fileStack = Stack<File>()
        fileStack.add(folder)
        output.append("<ul>")
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
                        var issues: Array<ProblemDescriptor>? = null
                        when (psiFile.javaClass.simpleName) {
                            "XmlFileImpl" -> {
                                val xmlInspection = XmlInspection()
                                issues =
                                    xmlInspection.checkFile(psiFile, InspectionManager.getInstance(project), false)
                            }

                            "PsiJavaFileImpl" -> {
                                val uastInspection = UastInspection()
                                issues =
                                    uastInspection.checkFile(psiFile, InspectionManager.getInstance(project), false)
                            }
                        }
                        if (issues.isNullOrEmpty()) continue
                        output.append("<li>${file.name} ")
                            .append("<span style=\"color:#7a7a7a;\">${file.absolutePath} ")
                            .append("<i>${issues.size} ${if (issues.size == 1) "problem" else "problems"}</i></span>")
                            .append("<ul>")
                        for (issue in issues) {
                            output.append("<li>${issue.descriptionTemplate}")
                                .append("<i style=\"color:#7a7a7a;\"> line ")
                                .append(issue.lineNumber.toString()).append("</i></li>")
                        }
                        output.append("</ul></li>")
                        break
                    }
                }
            }
        }
        output.append("</ul>")
    }
}