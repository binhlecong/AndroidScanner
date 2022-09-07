package com.github.binhlecong.androidscanner.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.util.xml.DomElement
import com.intellij.util.xml.DomElementVisitor
import com.intellij.util.xml.highlighting.BasicDomElementsInspection
// TODO: https://plugins.jetbrains.com/docs/intellij/xml-dom-api.html#introduction
class XmlResourceInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): XmlInspectionVisitor {
        return XmlInspectionVisitor()
    }
}

class XmlInspectionVisitor: DomElementVisitor {
    override fun visitDomElement(element: DomElement?) {
        element?.xmlElement.toString()
    }
}