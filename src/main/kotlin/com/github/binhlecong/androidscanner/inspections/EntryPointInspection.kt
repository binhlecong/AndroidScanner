package com.github.binhlecong.androidscanner.inspections

import com.github.binhlecong.androidscanner.visitors.CallExpressionVisitor
import com.intellij.codeInspection.*
import com.intellij.psi.PsiFile
import org.jetbrains.uast.*
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
                    if (node is UCallExpression) {
                        val args = node.valueArguments
                        var i = 0;
                        for (arg in args) {
                            if (arg is USimpleNameReferenceExpression) {
                                val resolvedVar = arg.resolve()
                                issueList.add(
                                    manager.createProblemDescriptor(
                                        resolvedVar ?: continue,
                                        "tracer $i ${resolvedVar}: ${node.asSourceString()}",
                                        isOnTheFly,
                                        LocalQuickFix.EMPTY_ARRAY,
                                        ProblemHighlightType.WARNING,
                                    )
                                )
                            }
                            i += 1
                        }
                    }
                }
                return false
            }
        })

        return issueList.toTypedArray()
    }
}