package com.github.binhlecong.androidscanner.inspections

import com.intellij.codeInspection.*
import com.intellij.psi.PsiFile
import com.intellij.psi.XmlRecursiveElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlFile

class XmlAttributeInspection : LocalInspectionTool() {
    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor> {
        if (file !is XmlFile) {
            return ProblemDescriptor.EMPTY_ARRAY
        }
        val issueList = arrayListOf<ProblemDescriptor>()

        file.accept(object : XmlRecursiveElementVisitor() {
            override fun visitXmlAttribute(attribute: XmlAttribute?) {
                if (attribute?.name?.split(':')?.last() == "exported") {
                    if (attribute.value == "true") {
                        issueList.add(
                            manager.createProblemDescriptor(
                                attribute,
                                "Found exported error",
                                isOnTheFly,
                                LocalQuickFix.EMPTY_ARRAY,
                                ProblemHighlightType.WARNING,
                            ),
                        )
                    }
                }
            }
        })

        return issueList.toTypedArray()
    }
}