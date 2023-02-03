package com.github.binhlecong.androidscanner.actions

import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.listeners.MyProjectManagerListener
import com.github.binhlecong.androidscanner.rules.RuleFile
import com.github.binhlecong.androidscanner.rules.RulesManager
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.InputStream
import java.net.URL

class RuleResetAction : AnAction() {

    /**
     * Gives the user feedback when the dynamic action menu is chosen.
     * Pops a simple message dialog. See the psi_demo plugin for an
     * example of how to use [AnActionEvent] to access data.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(event: AnActionEvent) {
        Config.PATH = "${MyProjectManagerListener.projectInstance!!.basePath.toString()}/${Config.RULE_DATA_DIR}"

        for (fileName in RuleFile.values()) {
            val rulesFile = File("${Config.PATH}/${fileName.fileName}")
            val inputStream: InputStream = URL("${Config.DEFAULT_RULE_URL}/${fileName.fileName}").openStream()
            IOUtils.copy(inputStream, rulesFile.outputStream())
        }

        val project = event.project ?: return
        RulesManager.updateRules(RuleFile.JAVA, project)
        RulesManager.updateRules(RuleFile.KOTLIN, project)
        RulesManager.updateRules(RuleFile.XML, project)
        NotificationGroupManager.getInstance()
            .getNotificationGroup("ArmorDroid Notification Group")
            .createNotification(
                "${Config.PLUGIN_NAME}'s rules have been reset to default",
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