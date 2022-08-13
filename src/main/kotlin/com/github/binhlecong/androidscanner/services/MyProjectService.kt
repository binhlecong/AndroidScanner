package com.github.binhlecong.androidscanner.services

import com.intellij.openapi.project.Project
import com.github.binhlecong.androidscanner.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
