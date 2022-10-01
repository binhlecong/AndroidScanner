package com.github.binhlecong.androidscanner.utils

import com.github.binhlecong.androidscanner.Config
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.xml.XmlAttribute
import org.jetbrains.rpc.LOG

class UastQuickFix(
    private val fixName: String,
    private val fixOld: String,
    private val fixNew: String,
    private val oldText: String = DEFAULT_OLD_TEXT,
) : LocalQuickFix {
    companion object {
        const val DEFAULT_OLD_TEXT = "_replace_this_"
    }

    override fun getFamilyName(): String {
        return fixName
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        try {
            val parentElement = descriptor.psiElement.parent
            val oldElementTextRange = parentElement?.textRange ?: return

            val editor = FileEditorManager.getInstance(project).selectedTextEditor
            val document = editor?.document ?: return

            val replacement = fixNew.replace(Config.templateString, oldText)
            val newSourceString = parentElement.text.replace(fixOld.toRegex(), replacement)
            document.replaceString(oldElementTextRange.startOffset, oldElementTextRange.endOffset, newSourceString)
        } catch (e: Exception) {
            LOG.error(e)
        }
//        try {
//            val parentElement = descriptor.psiElement.toUElement()?.uastParent
//            val replacement = fixNew.replace(Config.templateString, oldText)
//
//            val factory = KtPsiFactory(project)
//            val newSourceString = "{ ${parentElement?.asSourceString()?.replace(fixOld.toRegex(), replacement) ?: ""} }"
//
//            val newCodeBlock = factory.createBlock(newSourceString)
//            val resultElement = parentElement?.sourcePsiElement?.replace(newCodeBlock)
//
//            // Delete the front and end curly braces
//            resultElement?.firstChild?.delete()
//            resultElement?.lastChild?.delete()
//        } catch (e: Exception) {
//            LOG.error(e)
//        }
    }
}

class XmlAttrQuickFix(
    private val fixName: String,
    private val fixOld: String,
    private val fixNew: String,
    private val oldText: String = DEFAULT_OLD_TEXT,
) : LocalQuickFix {
    companion object {
        const val DEFAULT_OLD_TEXT = "_replace_this_"
    }

    override fun getFamilyName(): String {
        return fixName
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        try {
            val attribute = descriptor.psiElement as XmlAttribute
            attribute.setValue(fixNew)
        } catch (e: Exception) {
            LOG.error(e)
        }
    }
}