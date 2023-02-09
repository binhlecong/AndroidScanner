package com.github.binhlecong.androidscanner.listeners

import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.actions.AllowInspectionDialog
import com.github.binhlecong.androidscanner.rules.RuleFile
import com.github.binhlecong.androidscanner.services.MyProjectService
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.InputStream
import java.net.URL

internal class MyProjectManagerListener : ProjectManagerListener {
    override fun projectOpened(project: Project) {
        projectInstance = project
        Config.PATH = "${projectInstance!!.basePath.toString()}/${Config.RULE_DATA_DIR}"
        val directory = File(Config.PATH)
        val canMkdir = directory.mkdirs()
        if (!canMkdir) return

        val isPluginAllowed = AllowInspectionDialog(project).showAndGet()
        if (!isPluginAllowed) {
            return
        }

        for (fileName in RuleFile.values()) {
            val rulesFile = File("${Config.PATH}/${fileName.fileName}")
            if (!rulesFile.exists()) {
                val inputStream: InputStream = URL("${Config.DEFAULT_RULE_URL}/${fileName.fileName}").openStream()
                IOUtils.copy(inputStream, rulesFile.outputStream())
            }
        }
        project.getService(MyProjectService::class.java)
    }

    override fun projectClosing(project: Project) {
        projectInstance = null
        Config.PATH = ""
        super.projectClosing(project)
    }

    companion object {
        var projectInstance: Project? = null
    }
}
