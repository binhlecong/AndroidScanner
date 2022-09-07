package com.github.binhlecong.androidscanner.inspections

import com.android.tools.idea.lint.common.AndroidLintInspectionBase
import com.android.tools.idea.lint.common.LintIdeIssueRegistry
import com.android.tools.lint.detector.api.*
import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.Helper
import com.github.binhlecong.androidscanner.detectors.MethodIssueDetector
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.*

class MethodInspection : AndroidLintInspectionBase("Method Inspection", MethodIssueDetector.ISSUE) {

    init {
        val registry = LintIdeIssueRegistry()
        val myIssue = registry.getIssue(MethodIssueDetector.ISSUE.id)
        if (myIssue == null) {
            val list = registry.issues as MutableList<Issue>
            list.add(MethodIssueDetector.ISSUE)
        }
    }

    override fun getShortName(): String {
        return "MethodInspection"
    }
}