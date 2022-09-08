package com.github.binhlecong.androidscanner.detectors

import com.android.tools.lint.detector.api.*
import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.Helper
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.*

class MethodIssueDetector : Detector() {
    private val rules = Helper.loadRules(Config.PATH, Config.TYPE_METHOD)

    override fun getApplicableMethodNames(): List<String> {
        return Helper.getField(rules, Config.FIELD_methodName)
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        for (rule in rules) {
            val isInResources = context.evaluator.isMemberInClass(method, rule[Config.FIELD_className])
            if (rule[Config.FIELD_methodName] == node.methodName && isInResources) {
                if (rule[Config.FIELD_needFix].trim() != "1") {
                    context.report(
                        createMethodIssue(rule, this),
                        node,
                        context.getLocation(node),
                        rule[Config.FIELD_briefDescription],
                    )
                } else {
                    context.report(
                        createMethodIssue(rule, this),
                        node,
                        context.getLocation(node),
                        rule[Config.FIELD_briefDescription],
                        createMethodIssueQuickFix(context, node, rule),
                    )
                }
            }
        }
    }

    private fun createMethodIssue(rule: List<String>, detector: Detector): Issue {
        return Issue.create(
            id = rule[Config.FIELD_ID],
            briefDescription = rule[Config.FIELD_briefDescription],
            explanation = rule[Config.FIELD_explanation],
            category = Category.CORRECTNESS,
            priority = rule[Config.FIELD_priority].toInt(),
            severity = Severity.WARNING,
            androidSpecific = true,
            implementation = Implementation(
                detector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
            ),
        )
    }

    private fun createMethodIssueQuickFix(context: JavaContext, node: UCallExpression, rule: List<String>): LintFix {
        return LintFix.create()
            .name(rule[Config.FIELD_fixName])
            .replace()
            .range(context.getRangeLocation(node.uastParent!!, 0, node, 0))
            .pattern(rule[Config.FIELD_fixOld])
            .with(rule[Config.FIELD_fixNew].replace("\\n", "\n"))
            .autoFix()
            .reformat(true)
            .build()
    }

    companion object {
        @JvmField
        val ISSUE = Issue
            .create(
                id = "MethodIssueEntry",
                briefDescription = "empty",
                explanation = "empty",
                category = Category.SECURITY,
                priority = 0,
                severity = Severity.WARNING,
                androidSpecific = true,
                implementation = Implementation(
                    MethodIssueDetector::class.java,
                    Scope.JAVA_FILE_SCOPE,
                ),
            )
    }
}