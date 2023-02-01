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

class KotlinExpressionVisitor(
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
        if (exprClass == "KotlinUBlockExpression" ||
            exprClass == "KotlinUTryExpression" ||
            exprClass == "KotlinUQualifiedReferenceExpression"
        ) {
            return false
        }

        val rules = RulesManager.getKotlinRules()
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
