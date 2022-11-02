package com.github.binhlecong.androidscanner.inspections

import com.github.binhlecong.androidscanner.visitors.CallExpressionVisitor
import com.intellij.codeInspection.AbstractBaseUastLocalInspectionTool
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.psi.PsiFile
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UFile
import org.jetbrains.uast.toUElement
import org.jetbrains.uast.visitor.UastVisitor

class EntryPointInspection : AbstractBaseUastLocalInspectionTool(UFile::class.java) {
    //private val rules = Helper.loadRules(Config.PATH, Config.TYPE_CONSTRUCTOR)

//    fun loadRules() {
//
//    }

    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor> {
        val uFile = file.toUElement(UFile::class.java) ?: return ProblemDescriptor.EMPTY_ARRAY
        val issueList = arrayListOf<ProblemDescriptor>()

        uFile.accept(object : UastVisitor {
            override fun visitElement(node: UElement): Boolean {
                return false
            }

            override fun visitExpression(node: UExpression): Boolean {
                if (node::class.simpleName?.contains("UCallExpression") ?: false) {
                    node.accept(CallExpressionVisitor(manager, issueList, isOnTheFly))
                }
                return false
            }
        })

        return issueList.toTypedArray()
    }
}