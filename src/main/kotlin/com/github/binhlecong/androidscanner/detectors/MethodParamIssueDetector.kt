package com.github.binhlecong.androidscanner.detectors

import com.android.tools.lint.detector.api.*
import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.Helper
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.*

class MethodParamIssueDetector : Detector() {
    private val rules = Helper.loadRules(Config.PATH, Config.TYPE_METHOD_PARAM)

    override fun getApplicableMethodNames(): List<String> {
        return Helper.getField(rules, Config.FIELD_methodName)
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        for (rule in rules) {
            if (node.valueArgumentCount == 0)
                continue
            val isInResources = context.evaluator.isMemberInClass(method, rule[Config.FIELD_className])
            if (rule[Config.FIELD_methodName] == node.methodName && isInResources) {
                if (node.valueArgumentCount - 1 < rule[Config.FIELD_paramIndex].toInt())
                    continue

                val param = node.getArgumentForParameter(rule[Config.FIELD_paramIndex].toInt())!!.sourcePsi!!.text
                val reg = Regex(rule[Config.FIELD_paramPattern])
                if (param.matches(reg)) {

                    if (rule[Config.FIELD_needFix].trim() != "1") {
                        context.report(
                            createMethodParamIssue(rule, this),
                            node,
                            context.getLocation(node),
                            rule[Config.FIELD_briefDescription],
                        )
                    } else {
                        val oldText = getParamTextFromMethod(node, rule[Config.FIELD_paramIndex].toInt())
                        context.report(
                            createMethodParamIssue(rule, this),
                            node,
                            context.getLocation(node),
                            rule[Config.FIELD_briefDescription],
                            createMethodParamIssueQuickFix(context, node, rule, oldText),
                        )
                    }
                }
            }
        }
    }

    private fun getParamTextFromMethod(node: UCallExpression, index: Int): String {
        return node.getArgumentForParameter(index)!!.javaPsi!!.text
    }

    private fun createMethodParamIssue(rule: List<String>, detector: Detector): Issue {
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

    private fun createMethodParamIssueQuickFix(
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
                id = "MethodParamIssueEntry",
                briefDescription = "empty",
                explanation = "empty",
                category = Category.SECURITY,
                priority = 0,
                severity = Severity.WARNING,
                androidSpecific = true,
                implementation = Implementation(
                    MethodParamIssueDetector::class.java,
                    Scope.JAVA_FILE_SCOPE,
                ),
            )
    }
}