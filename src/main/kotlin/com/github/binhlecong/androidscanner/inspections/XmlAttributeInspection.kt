package com.github.binhlecong.androidscanner.inspections

import com.android.tools.idea.lint.common.AndroidLintInspectionBase
import com.github.binhlecong.androidscanner.detectors.XmlAttributeIssueDetector

class XmlAttributeInspection : AndroidLintInspectionBase("Xml Resource Inspection", XmlAttributeIssueDetector.ISSUE) {

//    init {
//        val registry = LintIdeIssueRegistry()
//        val myIssue = registry.getIssue(XmlAttributeIssueDetector.ISSUE.id)
//        if (myIssue == null) {
//            val list = registry.issues as MutableList<Issue>
//            list.add(XmlAttributeIssueDetector.ISSUE)
//        }
//    }

    override fun getShortName(): String {
        return "XmlResourceInspection"
    }
}