package com.github.binhlecong.androidscanner.detectors

import com.android.tools.lint.detector.api.*
import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.Helper
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.*

class ConstructorIssueDetector : Detector() {
    private val rules = Helper.loadRules(Config.PATH, Config.TYPE_CONSTRUCTOR)

    override fun getApplicableConstructorTypes(): List<String> {
        return Helper.getField(rules, Config.FIELD_className)
    }

    override fun visitConstructor(context: JavaContext, node: UCallExpression, constructor: PsiMethod) {
        for (rule in rules) {
            // java.util.Random => Random
            if (rule[Config.FIELD_className].split('.').last() == constructor.name) {
                if (rule[Config.FIELD_needFix].trim() != "1") {
                    context.report(
                        createConstructorIssue(rule, this),
                        node,
                        context.getLocation(node),
                        rule[Config.FIELD_briefDescription]
                    )
                } else {
                    val oldText = getVariableNameFromDeclare(node)
                    context.report(
                        createConstructorIssue(rule, this),
                        node,
                        context.getLocation(node),
                        rule[Config.FIELD_briefDescription],
                        createConstructorIssueQuickFix(context, node, rule, oldText)
                    )
                }
            }
        }
    }

    private fun getVariableNameFromDeclare(node: UCallExpression): String {
        for (element in node.uastParent!!.javaPsi!!.children) {
            if (element.text.equals("=")) {
                return if (element.prevSibling.text.isBlank()) {
                    element.prevSibling.prevSibling.text
                } else {
                    element.prevSibling.text
                }
            }
        }
        return ""
    }

    private fun createConstructorIssue(rule: List<String>, detector: Detector): Issue {
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
                EnumSet.of(Scope.JAVA_FILE)
            )
        )
    }

    private fun createConstructorIssueQuickFix(
        context: JavaContext,
        node: UCallExpression,
        rule: List<String>,
        oldText: String,
    ): LintFix {
        return LintFix.create()
            .name(rule[Config.FIELD_fixName])
            .replace()
            .range(context.getRangeLocation(node.uastParent!!, 0, node, 0))
            .pattern(rule[Config.FIELD_fixOld])
            .with(rule[Config.FIELD_fixNew].replace(Config.templateString, oldText).replace("\\n", "\n"))
            .autoFix()
            .reformat(true)
            .build()
    }

    companion object {
        @JvmField
        val ISSUE = Issue
            .create(
                id = "ConstructorIssueEntry",
                briefDescription = "",
                explanation = "",
                category = Category.SECURITY,
                priority = 0,
                severity = Severity.WARNING,
                androidSpecific = true,
                implementation = Implementation(
                    ConstructorIssueDetector::class.java,
                    Scope.JAVA_FILE_SCOPE,
                )
            )
    }
}