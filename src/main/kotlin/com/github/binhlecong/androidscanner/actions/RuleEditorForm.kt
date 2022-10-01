package com.github.binhlecong.androidscanner.actions

import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.Helper
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.table.DefaultTableModel

class RuleEditorForm : DialogWrapper(true) {
    private val panel = JPanel(BorderLayout())
    private val dataList = Helper.loadRules(Config.PATH, "")
    private val dataArray = Helper.convertListListToArrayArray(dataList)
    private val columns = Config.FIELD_NAMES
    private var tableModel = DefaultTableModel(dataArray, columns)
    private val table = JBTable(tableModel)

    override fun createCenterPanel(): JComponent {
        val addButton = JButton("Add rule")
        addButton.addActionListener {
            tableModel.insertRow(tableModel.rowCount, arrayOf("", "", "", "", "", "", "", "", "", "", "", "", ""))
            table.changeSelection(tableModel.rowCount - 1, 0, false, false)
        }

        panel.preferredSize = Dimension(Config.ACTION_SIZE_WIDTH, Config.ACTION_SIZE_HEIGHT)
        panel.add(JScrollPane(table), BorderLayout.NORTH)
        panel.add(addButton, BorderLayout.SOUTH)

        return panel
    }

    init {
        title = "Rules Editor"
        init()
    }

    override fun doOKAction() {
        Helper.saveRules(Config.PATH, tableModel)
        super.doOKAction()
    }
}