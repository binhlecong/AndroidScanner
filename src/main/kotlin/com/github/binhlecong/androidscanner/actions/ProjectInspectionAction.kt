package com.github.binhlecong.androidscanner.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ProjectInspectionAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        ProjectInspectionForm(e.project).show()
    }
}