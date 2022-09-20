package com.github.binhlecong.androidscanner.inspections

import com.intellij.codeInspection.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.XmlRecursiveElementVisitor
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlFile
import com.intellij.util.xml.DomManager

class XmlAttributeInspection : LocalInspectionTool() {
    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor> {
        if (file !is XmlFile) {
            return ProblemDescriptor.EMPTY_ARRAY
        }
        val issueList = arrayListOf<ProblemDescriptor>()

        file.accept(object : XmlRecursiveElementVisitor() {
            override fun visitXmlAttributeValue(value: XmlAttributeValue?) {
                issueList.add(
                    manager.createProblemDescriptor(
                        value as PsiElement,
                        value?.value ?: "error",
                        isOnTheFly,
                        LocalQuickFix.EMPTY_ARRAY,
                        ProblemHighlightType.WARNING,
                    ),
                )
            }
        })

        return issueList.toTypedArray()
    }
}