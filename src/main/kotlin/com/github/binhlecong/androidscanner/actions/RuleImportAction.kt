package com.github.binhlecong.androidscanner.actions

import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.rules.RuleFile
import com.github.binhlecong.androidscanner.rules.RulesManager
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory


class RuleImportAction : AnAction() {

    /**
     * Gives the user feedback when the dynamic action menu is chosen.
     * Pops a simple message dialog. See the psi_demo plugin for an
     * example of how to use [AnActionEvent] to access data.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(event: AnActionEvent) {
        val selectedFiles = FileChooser.chooseFiles(
            FileChooserDescriptorFactory.createSingleFileDescriptor(Config.PLUGIN_FILE_EXT),
            event.project,
            null
        )

        if (selectedFiles.isEmpty())
            return

        val project = event.project ?: return
        val firstFile = selectedFiles[0]
        if (firstFile.extension != Config.PLUGIN_FILE_EXT || firstFile.isDirectory) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("ArmorDroid Notification Group")
                .createNotification(
                    "${Config.PLUGIN_NAME} failed to import the chosen file",
                    NotificationType.ERROR
                )
                .notify(project)
            return
        }

        RulesManager.importCustomRules(firstFile.inputStream)

        RulesManager.updateRules(RuleFile.JAVA, project)
        RulesManager.updateRules(RuleFile.KOTLIN, project)
        RulesManager.updateRules(RuleFile.XML, project)

        NotificationGroupManager.getInstance()
            .getNotificationGroup("ArmorDroid Notification Group")
            .createNotification(
                "${Config.PLUGIN_NAME} has imported new rules",
                NotificationType.INFORMATION
            )
            .notify(project)
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