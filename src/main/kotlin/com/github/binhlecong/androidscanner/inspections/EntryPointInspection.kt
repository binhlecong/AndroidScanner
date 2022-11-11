package com.github.binhlecong.androidscanner.inspections

import com.github.binhlecong.androidscanner.visitors.uast_visitors.CallExpressionVisitor
import com.intellij.codeInspection.AbstractBaseUastLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.psi.PsiFile
import org.jetbrains.uast.UFile
import org.jetbrains.uast.toUElement

class EntryPointInspection : AbstractBaseUastLocalInspectionTool(UFile::class.java) {
    //private val rules = Helper.loadRules(Config.PATH, Config.TYPE_CONSTRUCTOR)

//    fun loadRules() {
//
//    }

    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor> {
        val uFile = file.toUElement(UFile::class.java) ?: return ProblemDescriptor.EMPTY_ARRAY
        val issues = arrayListOf<ProblemDescriptor>()
        uFile.accept(CallExpressionVisitor(manager, isOnTheFly, issues))
        return issues.toTypedArray()
    }
}