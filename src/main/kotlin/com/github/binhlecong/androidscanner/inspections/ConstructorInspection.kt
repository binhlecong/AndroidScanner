package com.github.binhlecong.androidscanner.inspections

import com.android.tools.idea.lint.common.AndroidLintInspectionBase
import com.android.tools.idea.lint.common.LintIdeIssueRegistry
import com.android.tools.lint.detector.api.Issue
import com.github.binhlecong.androidscanner.detectors.ConstructorIssueDetector

class ConstructorInspection : AndroidLintInspectionBase("Constructor Inspection", ConstructorIssueDetector.ISSUE) {

    init {
        val registry = LintIdeIssueRegistry()
        val myIssue = registry.getIssue(ConstructorIssueDetector.ISSUE.id)
        if (myIssue == null) {
            val list = registry.issues as MutableList<Issue>
            list.add(ConstructorIssueDetector.ISSUE)
        }
    }

    override fun getShortName(): String {
        return "ConstructorInspection"
    }
}