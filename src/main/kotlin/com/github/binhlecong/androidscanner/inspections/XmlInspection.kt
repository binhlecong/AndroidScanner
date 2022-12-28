package com.github.binhlecong.androidscanner.inspections

import com.github.binhlecong.androidscanner.rules.RulesManager
import com.intellij.codeInspection.*
import com.intellij.psi.PsiFile
import com.intellij.psi.XmlRecursiveElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag

class XmlInspection : LocalInspectionTool() {
    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor> {
        if (file !is XmlFile) {
            return ProblemDescriptor.EMPTY_ARRAY
        }
        val issues = arrayListOf<ProblemDescriptor>()

        val rules = RulesManager.getXmlRules()
        file.accept(object : XmlRecursiveElementVisitor(true) {
            override fun visitXmlAttribute(attribute: XmlAttribute?) {

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

//            override fun visitXmlTag(tag: XmlTag?) {
//                issues.add(
//                    manager.createProblemDescriptor(
//                        tag ?: return,
//                        tag::class.simpleName + ": " + tag.text,
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//            }
//
//            override fun visitXmlElement(element: XmlElement?) {
//                issues.add(
//                    manager.createProblemDescriptor(
//                        element ?: return,
//                        element::class.simpleName + ": " + element.text,
//                        isOnTheFly,
//                        LocalQuickFix.EMPTY_ARRAY,
//                        ProblemHighlightType.WARNING,
//                    )
//                )
//            }
        })

        return issues.toTypedArray()
    }
}