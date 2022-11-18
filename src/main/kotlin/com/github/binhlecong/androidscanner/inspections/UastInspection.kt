package com.github.binhlecong.androidscanner.inspections

import com.github.binhlecong.androidscanner.visitors.uast_visitors.JavaExpressionVisitor
import com.github.binhlecong.androidscanner.visitors.uast_visitors.KotlinExpressionVisitor
import com.intellij.codeInspection.AbstractBaseUastLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.psi.PsiFile
import org.jetbrains.uast.UFile
import org.jetbrains.uast.toUElement

class UastInspection : AbstractBaseUastLocalInspectionTool(UFile::class.java) {
    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor> {
        val uFile = file.toUElement(UFile::class.java) ?: return ProblemDescriptor.EMPTY_ARRAY
        val issues = arrayListOf<ProblemDescriptor>()
        when (file::class.simpleName) {
            "PsiJavaFileImpl" -> uFile.accept(JavaExpressionVisitor(manager, isOnTheFly, issues))
            "KtFile" -> uFile.accept(KotlinExpressionVisitor(manager, isOnTheFly, issues))
        }
        return issues.toTypedArray()
    }
}