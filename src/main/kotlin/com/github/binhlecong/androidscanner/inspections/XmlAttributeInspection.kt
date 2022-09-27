package com.github.binhlecong.androidscanner.inspections

import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.Helper
import com.github.binhlecong.androidscanner.utils.XmlAttrQuickFix
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.psi.PsiFile
import com.intellij.psi.XmlRecursiveElementVisitor
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlFile


class XmlAttributeInspection : LocalInspectionTool() {
    private val rules = Helper.loadRules(Config.PATH, Config.TYPE_XML_ATTRIBUTE)

    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor> {
        if (file !is XmlFile) {
            return ProblemDescriptor.EMPTY_ARRAY
        }
        val issueList = arrayListOf<ProblemDescriptor>()

        file.accept(object : XmlRecursiveElementVisitor() {
            override fun visitXmlAttribute(attribute: XmlAttribute?) {
                for (rule in rules) {
                    val xmlTag = rule[Config.FIELD_XML_TAG]
                    val xmlAttribute = rule[Config.FIELD_XML_ATTRIBUTE]
                    val xmlAttributeValue = rule[Config.FIELD_XML_ATTRIBUTE_VALUE]

                    if (attribute?.name?.split(':')?.last() == xmlAttribute) {
                        if (attribute.value == xmlAttributeValue) {
                            val briefDescription = rule[Config.FIELD_BRIEF_DESCRIPTION]
                            val needFix = rule[Config.FIELD_NEED_FIX].trim() == "1"
                            var fixes = emptyArray<XmlAttrQuickFix>()

                            if (needFix) {
                                fixes += XmlAttrQuickFix(
                                    rule[Config.FIELD_FIX_NAME],
                                    rule[Config.FIELD_FIX_OLD],
                                    rule[Config.FIELD_FIX_NEW],
                                )
                            }

                            issueList.add(
                                manager.createProblemDescriptor(
                                    attribute,
                                    briefDescription,
                                    isOnTheFly,
                                    fixes,
                                    ProblemHighlightType.WARNING,
                                )
                            )
                        }
                    }
                }
            }
        })

        return issueList.toTypedArray()
    }
}