package com.github.binhlecong.androidscanner.inspections

import com.android.tools.idea.lint.common.AndroidLintInspectionBase
import com.github.binhlecong.androidscanner.detectors.MethodIssueDetector

class MethodInspection : AndroidLintInspectionBase("Method Inspection", MethodIssueDetector.ISSUE) {

//    init {
//        val registry = LintIdeIssueRegistry()
//        val myIssue = registry.getIssue(MethodIssueDetector.ISSUE.id)
//        if (myIssue == null) {
//            val list = registry.issues as MutableList<Issue>
//            list.add(MethodIssueDetector.ISSUE)
//        }
//    }

    override fun getShortName(): String {
        return "MethodInspection"
    }
}