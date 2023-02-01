package com.github.binhlecong.androidscanner.services

import com.github.binhlecong.androidscanner.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {
    init {
        println(MyBundle.message("projectService", project.name))
    }
}
