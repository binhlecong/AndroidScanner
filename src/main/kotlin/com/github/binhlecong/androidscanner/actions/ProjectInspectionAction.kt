package com.github.binhlecong.androidscanner.actions

import com.github.binhlecong.androidscanner.Config
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.RegisterToolWindowTask
import com.intellij.openapi.wm.ToolWindowAnchor
import com.intellij.openapi.wm.ToolWindowManager
import icons.MyIcons
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
            ToolWindowManager.getInstance(e.project!!).registerToolWindow(
                RegisterToolWindowTask(
                    id = "ArmorDroid",
                    anchor = ToolWindowAnchor.BOTTOM,
                    component = createResultPanel(resultString),
                    sideTool = false,
                    canCloseContent = true,
                    canWorkInDumbMode = false,
                    shouldBeAvailable = true,
                    contentFactory = null,
                    icon = MyIcons.PluginIcon,
                    stripeTitle = null,
                )
            ).show()
        }
    }

    private fun createResultPanel(result: String): JPanel {
        return ProjInspectionResultForm(result).rootPanel
    }
}