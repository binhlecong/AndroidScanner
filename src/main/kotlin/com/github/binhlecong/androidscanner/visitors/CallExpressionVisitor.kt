package com.github.binhlecong.androidscanner.visitors

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.visitor.UastVisitor

class CallExpressionVisitor(
    private val manager: InspectionManager,
    private val issueList: ArrayList<ProblemDescriptor>,
    private val isOnTheFly: Boolean,
) : UastVisitor {
    override fun visitElement(node: UElement): Boolean {
        return false
    }

    override fun visitExpression(node: UExpression): Boolean {
        if (node::class.simpleName?.contains("UCallExpression") ?: false) {
            val sourcePsi = node.sourcePsi ?: return false
            issueList.add(
                manager.createProblemDescriptor(
                    sourcePsi,
                    "${node::class.simpleName}: ${node.asSourceString()}",
                    isOnTheFly,
                    LocalQuickFix.EMPTY_ARRAY,
                    ProblemHighlightType.WARNING,
                )
            )
            if (node is UCallExpression) {
                val args = node.valueArguments
                var i = 0;
                for (arg in args) {
                    if (arg is USimpleNameReferenceExpression) {
                        val resolvedVar = arg.resolve()
                        issueList.add(
                            manager.createProblemDescriptor(
                                resolvedVar ?: continue,
                                "Traced ref $i ${resolvedVar}: ${node.asSourceString()}",
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
//    override fun visitElement(node: UElement): Boolean {
//        val sourcePsi = node.sourcePsi ?: return false
//
//        if (node::class.simpleName?.contains("USimpleNameReferenceExpression") ?: false) {
//            val refDeclaration = node.tryResolve()
//            if (refDeclaration != null) {
//                issueList.add(
//                    manager.createProblemDescriptor(
//                        refDeclaration,
//                        "${refDeclaration::class.simpleName}: ${refDeclaration.text}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//            }
//        }
//        if (node::class.simpleName?.contains("UCallExpression") ?: false) {
//            issueList.add(
//                manager.createProblemDescriptor(
//                    sourcePsi,
//                    "${node::class.simpleName}: ${node.asSourceString()}",
//                    isOnTheFly,
//                    LocalQuickFix.EMPTY_ARRAY,
//                    ProblemHighlightType.WARNING,
//                )
//            )
//        }
//        if (node::class.simpleName?.contains("ULiteralExpression") ?: false) {
//            issueList.add(
//                manager.createProblemDescriptor(
//                    sourcePsi,
//                    "${node::class.simpleName}: ${node.asSourceString()}",
//                    isOnTheFly,
//                    LocalQuickFix.EMPTY_ARRAY,
//                    ProblemHighlightType.WARNING,
//                )
//            )
//        }
//        // todo: use this to check expr class name
////        issueList.add(
////            manager.createProblemDescriptor(
////                sourcePsi,
////                "${node::class.simpleName}: ${node.asSourceString()}",
////                isOnTheFly,
////                LocalQuickFix.EMPTY_ARRAY,
////                ProblemHighlightType.WARNING,
////            )
////        )
//        return false
//    }
}