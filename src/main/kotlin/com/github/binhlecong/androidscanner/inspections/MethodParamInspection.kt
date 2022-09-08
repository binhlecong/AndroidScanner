package com.github.binhlecong.androidscanner.inspections

import com.android.tools.idea.lint.common.AndroidLintInspectionBase
import com.github.binhlecong.androidscanner.detectors.MethodParamIssueDetector

class MethodParamInspection : AndroidLintInspectionBase("Method Parameter Inspection", MethodParamIssueDetector.ISSUE) {

//    init {
//        val registry = LintIdeIssueRegistry()
//        val myIssue = registry.getIssue(MethodParamIssueDetector.ISSUE.id)
//        if (myIssue == null) {
//            val list = registry.issues as MutableList<Issue>
//            list.add(MethodParamIssueDetector.ISSUE)
//        }
//    }

    override fun getShortName(): String {
        return "MethodParamInspection"
    }
}