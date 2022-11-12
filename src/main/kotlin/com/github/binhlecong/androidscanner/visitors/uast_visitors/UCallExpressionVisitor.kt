package com.github.binhlecong.androidscanner.visitors.uast_visitors

import com.github.binhlecong.androidscanner.rules.RulesManager
import com.github.binhlecong.androidscanner.strategies.UastInspectionStrategy
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.visitor.UastVisitor

class UCallExpressionVisitor(
    private val manager: InspectionManager,
    private val isOnTheFly: Boolean,
    private val issues: ArrayList<ProblemDescriptor>,
) : UastVisitor {

    override fun visitElement(node: UElement): Boolean {
        return false
    }

    override fun visitExpression(node: UExpression): Boolean {
        val sourcePsi = node.sourcePsi ?: return false
        if (node !is UCallExpression) return false
        val rules = RulesManager().getUastRules(node.asSourceString())
        for (rule in rules) {
            val inspector = rule.inspector as UastInspectionStrategy
            val highlightType = rule.highlightType

            if (inspector.isSecurityIssue(node)) {
                issues.add(
                    manager.createProblemDescriptor(
                        sourcePsi,
                        inspector.toString(),
                        isOnTheFly,
                        inspector.buildFixes(node),
                        highlightType,
                    )
                )
            }
        }
        return false
    }
}
//            if (node::class.simpleName?.contains("UCallExpression") == true) {
//                val sourcePsi = node.sourcePsi ?: return false
//                issues.add(
//                    manager.createProblemDescriptor(
//                        sourcePsi,
//                        "${node::class.simpleName}: ${node.asSourceString()}",
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//                if (node is UCallExpression) {
//                    val args = node.valueArguments
//                    var i = 0;
//                    for (arg in args) {
//                        if (arg is USimpleNameReferenceExpression) {
//                            val resolvedVar = arg.resolve()
//                            issues.add(
//                                manager.createProblemDescriptor(
//                                    resolvedVar ?: continue,
//                                    "Traced ref $i ${resolvedVar}: ${node.asSourceString()}",
//                                    isOnTheFly,
//                                    LocalQuickFix.EMPTY_ARRAY,
//                                    ProblemHighlightType.WARNING,
//                                )
//                            )
//                        }
//                        i += 1
//                    }
//                }
    //}

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
