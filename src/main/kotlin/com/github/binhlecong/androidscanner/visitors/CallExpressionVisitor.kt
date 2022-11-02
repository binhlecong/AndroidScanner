package com.github.binhlecong.androidscanner.visitors

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import org.jetbrains.uast.UElement
import org.jetbrains.uast.tryResolve
import org.jetbrains.uast.visitor.UastVisitor

class CallExpressionVisitor(
    private val manager: InspectionManager,
    private val issueList: ArrayList<ProblemDescriptor>,
    private val isOnTheFly: Boolean,
) : UastVisitor {
    override fun visitElement(node: UElement): Boolean {
        val sourcePsi = node.sourcePsi ?: return false

        if (node::class.simpleName?.contains("USimpleNameReferenceExpression") ?: false) {
            val refDeclaration = node.tryResolve()
            if (refDeclaration != null) {
                issueList.add(
                    manager.createProblemDescriptor(
                        refDeclaration,
                        "${node::class.simpleName}: ${refDeclaration.text}",
                        isOnTheFly,
                        LocalQuickFix.EMPTY_ARRAY,
                        ProblemHighlightType.WARNING,
                    )
                )
            }
        }
        if (node::class.simpleName?.contains("UCallExpression") ?: false) {
            issueList.add(
                manager.createProblemDescriptor(
                    sourcePsi,
                    "${node::class.simpleName}: ${node.asSourceString()}",
                    isOnTheFly,
                    LocalQuickFix.EMPTY_ARRAY,
                    ProblemHighlightType.WARNING,
                )
            )
        }
//        issueList.add(
//            manager.createProblemDescriptor(
//                sourcePsi,
//                "${node::class.simpleName}: ${node.asSourceString()}",
//                isOnTheFly,
//                LocalQuickFix.EMPTY_ARRAY,
//                ProblemHighlightType.WARNING,
//            )
//        )
        return false
    }
//
//    override fun visitCallableReferenceExpression(node: UCallableReferenceExpression): Boolean {
//        val sourcePsi = node.sourcePsi ?: return false
//
//        issueList.add(
//            manager.createProblemDescriptor(
//                sourcePsi,
//                "CallableRefExpr: ${node::class.simpleName}: ${node.asSourceString()}",
//                isOnTheFly,
//                LocalQuickFix.EMPTY_ARRAY,
//                ProblemHighlightType.WARNING,
//            )
//        )
//        return false
//    }
//
//    override fun visitSimpleNameReferenceExpression(node: USimpleNameReferenceExpression): Boolean {
//        val sourcePsi = node.sourcePsi ?: return false
//
//        issueList.add(
//            manager.createProblemDescriptor(
//                sourcePsi,
//                "SimpleNameRefExpr: ${node::class.simpleName}: ${node.asSourceString()}",
//                isOnTheFly,
//                LocalQuickFix.EMPTY_ARRAY,
//                ProblemHighlightType.WARNING,
//            )
//        )
//        return false
//    }
//
//    override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression): Boolean {
//        val sourcePsi = node.sourcePsi ?: return false
//
//        issueList.add(
//            manager.createProblemDescriptor(
//                sourcePsi,
//                "QualifiedRefExpr: ${node::class.simpleName}: ${node.asSourceString()}",
//                isOnTheFly,
//                LocalQuickFix.EMPTY_ARRAY,
//                ProblemHighlightType.WARNING,
//            )
//        )
//        return false
//    }
//
//    override fun visitTypeReferenceExpression(node: UTypeReferenceExpression): Boolean {
//        val sourcePsi = node.sourcePsi ?: return false
//
//        issueList.add(
//            manager.createProblemDescriptor(
//                sourcePsi,
//                "TypeRefExpr: ${node::class.simpleName}: ${node.asSourceString()}",
//                isOnTheFly,
//                LocalQuickFix.EMPTY_ARRAY,
//                ProblemHighlightType.WARNING,
//            )
//        )
//        return false
//    }
//
//    override fun visitLiteralExpression(node: ULiteralExpression): Boolean {
//        val sourcePsi = node.sourcePsi ?: return false
//
//        issueList.add(
//            manager.createProblemDescriptor(
//                sourcePsi,
//                "LiteralExpr: ${node::class.simpleName}: ${node.asSourceString()}",
//                isOnTheFly,
//                LocalQuickFix.EMPTY_ARRAY,
//                ProblemHighlightType.WARNING,
//            )
//        )
//        return false
//    }
//
//    override fun visitMethod(node: UMethod): Boolean {
//        val sourcePsi = node.sourcePsi ?: return false
//
//        issueList.add(
//            manager.createProblemDescriptor(
//                sourcePsi,
//                "Method: ${node::class.simpleName}: ${node.asSourceString()}",
//                isOnTheFly,
//                LocalQuickFix.EMPTY_ARRAY,
//                ProblemHighlightType.WARNING,
//            )
//        )
//        return false
//    }
}