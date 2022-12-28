package com.github.binhlecong.androidscanner.visitors.xml_visitor

import com.github.binhlecong.androidscanner.rules.RulesManager
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.psi.XmlRecursiveElementVisitor
import com.intellij.psi.xml.XmlAttribute

class XmlAttributeVisitor(
    private val manager: InspectionManager,
    private val isOnTheFly: Boolean,
    private val issues: ArrayList<ProblemDescriptor>,
) : XmlRecursiveElementVisitor(true) {
    override fun visitXmlAttribute(attribute: XmlAttribute?) {
        val rules = RulesManager.getXmlRules()

        for (rule in rules) {
            val inspector = rule.inspector
            val highlightType = rule.highlightType

            if (inspector.isSecurityIssue(attribute ?: continue)) {
                issues.add(
                    manager.createProblemDescriptor(
                        attribute,
                        attribute::class.simpleName + ": " + rule.briefDescription,
                        isOnTheFly,
                        rule.fixes.toTypedArray(),
                        ProblemHighlightType.WARNING,
                    )
                )
            }
        }
    }
}