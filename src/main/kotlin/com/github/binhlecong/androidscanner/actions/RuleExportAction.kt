package com.github.binhlecong.androidscanner.actions

import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.rules.RulesManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.awt.Desktop
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JFrame


class RuleExportAction : AnAction() {

    /**
     * Gives the user feedback when the dynamic action menu is chosen.
     * Pops a simple message dialog. See the psi_demo plugin for an
     * example of how to use [AnActionEvent] to access data.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(event: AnActionEvent) {
        val parentFrame = JFrame()
        val fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Export ${Config.PLUGIN_NAME} Rule Data"
        fileChooser.selectedFile = File("my_custom_rules.${Config.PLUGIN_FILE_EXT}")
        val userSelection = fileChooser.showSaveDialog(parentFrame)

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            val fileToSave = fileChooser.selectedFile
            RulesManager.exportCustomRules(fileToSave.absolutePath)
            Desktop.getDesktop().open(fileToSave.parentFile)
        }
    }

    /**
     * Determines whether this menu item is available for the current context.
     * Requires a project to be open.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    override fun update(e: AnActionEvent) {
        // Set the availability based on whether a project is open
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }
}