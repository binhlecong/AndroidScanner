package com.github.binhlecong.androidscanner.actions

import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.Helper
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.table.DefaultTableModel

class LibraryScanForm(val project: Project) : DialogWrapper(true) {
    private lateinit var panel: JPanel

    //    private val dataList = Helper.loadRules(Config.PATH, "")
    //    private val dataArray = Helper.convertListListToArrayArray(dataList)
    private lateinit var table: JBTable

    override fun createCenterPanel(): JComponent {
        val libraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project)
        val libraries = libraryTable.libraries
        val libraryInfo = libraries.map { listOf(it.name ?: "unknown_lib", it.presentableName) }
        val dataArray = Helper.convertListListToArrayArray(libraryInfo)
        val columns = arrayOf("name", "presentableName")
        val tableModel = DefaultTableModel(dataArray, columns)

        table = JBTable(tableModel)

        panel = JPanel(BorderLayout())
        panel.preferredSize = Dimension(Config.ACTION_SIZE_WIDTH, Config.ACTION_SIZE_HEIGHT)
        val scrollPane = JBScrollPane(table)
        panel.add(scrollPane, BorderLayout.NORTH)
        return panel
    }

    init {
        title = "SCA Inspection Result"
        init()
    }
}