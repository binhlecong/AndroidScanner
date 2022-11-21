package com.github.binhlecong.androidscanner.listeners

import com.github.binhlecong.androidscanner.Config
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
        Config.PATH = projectInstance!!.basePath.toString() + "/security-rules"
        val directory = File(Config.PATH)
        val canMkdir = directory.mkdirs()
        if (!canMkdir) return
        for (fileName in Config.RULES_FILES) {
            val rulesFile = File(Config.PATH + "/" + fileName)
            if (!rulesFile.exists()) {
                val inputStream: InputStream = URL(Config.RULES_URL + "/" + fileName).openStream()
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
