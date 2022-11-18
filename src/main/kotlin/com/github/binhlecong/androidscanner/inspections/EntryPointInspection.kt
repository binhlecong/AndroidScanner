package com.github.binhlecong.androidscanner.inspections

import com.github.binhlecong.androidscanner.visitors.uast_visitors.JavaExpressionVisitor
import com.intellij.codeInspection.AbstractBaseUastLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.psi.PsiFile
import org.jetbrains.uast.UFile
import org.jetbrains.uast.toUElement

// TODO: change to pure local inspection to support other file types
class EntryPointInspection : AbstractBaseUastLocalInspectionTool(UFile::class.java) {
    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor> {
        val uFile = file.toUElement(UFile::class.java) ?: return ProblemDescriptor.EMPTY_ARRAY
        val issues = arrayListOf<ProblemDescriptor>()
        // todo: switch between java, kotlin, xml, gradle
        uFile.accept(JavaExpressionVisitor(manager, isOnTheFly, issues))
        return issues.toTypedArray()
    }
}