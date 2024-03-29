package com.github.binhlecong.androidscanner.visitors.uast_visitors

import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy
import com.github.binhlecong.androidscanner.rules.RulesManager
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.visitor.UastVisitor

class JavaExpressionVisitor(
    private val manager: InspectionManager,
    private val isOnTheFly: Boolean,
    private val issues: ArrayList<ProblemDescriptor>,
) : UastVisitor {

    override fun visitElement(node: UElement): Boolean {
        return false
    }

    override fun visitExpression(node: UExpression): Boolean {
        val sourcePsi = node.sourcePsi ?: return false

        val exprClass = node::class.simpleName
        if (exprClass == "JavaUCodeBlockExpression" ||
            exprClass == "JavaUTryExpression" ||
            exprClass == "JavaUCompositeQualifiedExpression"
        ) {
            return false
        }

        val rules = RulesManager.getJavaRules()
        for (rule in rules) {
            if (!rule.enabled) continue

            val inspector = rule.inspector
            val highlightType = rule.highlightType

            if (UastInspectionStrategy.isSecurityIssue(node, inspector)) {
                issues.add(
                    manager.createProblemDescriptor(
                        sourcePsi,
                        Config.PLUGIN_NAME + ": " + rule.briefDescription,
                        isOnTheFly,
                        rule.fixes.toTypedArray(),
                        ProblemHighlightType.WARNING,
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
//
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
