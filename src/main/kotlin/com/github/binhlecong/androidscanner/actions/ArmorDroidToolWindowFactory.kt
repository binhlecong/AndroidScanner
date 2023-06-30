// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.github.binhlecong.androidscanner.actions

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel

class ArmorDroidToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val toolWindowContent = CalendarToolWindowContent(toolWindow)
        val content = ContentFactory.SERVICE.getInstance()
            .createContent(toolWindowContent.getToolWindowContentPanel(), "Instruction", false)
        toolWindow.contentManager.addContent(content)
    }

    private class CalendarToolWindowContent(toolWindow: ToolWindow) {
        val contentPanel = JPanel()
        private val instructionJLabel =
            JLabel("<html>Go to <b>Tools -> ArmorDroid Secure Coding -> Inspect Whole Project</b>")

        init {
            contentPanel.layout = BorderLayout(0, 20)
            contentPanel.border = BorderFactory.createEmptyBorder(40, 0, 0, 0)
            contentPanel.add(createCalendarPanel(), BorderLayout.CENTER)
        }

        private fun createCalendarPanel(): JPanel {
            val calendarPanel = JPanel()
            calendarPanel.add(instructionJLabel)
            return calendarPanel
        }


        fun getToolWindowContentPanel(): JPanel {
            return contentPanel
        }
    }
}