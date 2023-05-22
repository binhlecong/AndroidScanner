package com.github.binhlecong.androidscanner.actions

import com.github.binhlecong.androidscanner.Config
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.ToolWindowManager
import java.io.File
import javax.swing.JOptionPane
import javax.swing.JPanel

class ProjectInspectionAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        ProjectInspectionForm(e.project).show()
        val resultString: String
        try {
            val inputStream = File(ProjectInspectionForm.logFile).inputStream()
            resultString = inputStream.reader().use { it.readText() }
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(null, e.message, Config.PLUGIN_NAME, JOptionPane.ERROR_MESSAGE)
            //throw RuntimeException(e)
            return
        }
        if (e.project != null) {
            val contentManager =
                ToolWindowManager.getInstance(e.project!!).getToolWindow("ArmorDroid")?.contentManager ?: return
            val content = contentManager.factory.createContent(
                createResultPanel(resultString),
                "Full project inspection",
                false,
            )
            contentManager.addContent(content)

        }
    }

    private fun createResultPanel(result: String): JPanel {
        return ProjInspectionResultForm(result).rootPanel
    }
}