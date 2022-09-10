package com.github.binhlecong.androidscanner.utils

import com.github.binhlecong.androidscanner.Config
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import org.jetbrains.rpc.LOG
import org.jetbrains.uast.sourcePsiElement
import org.jetbrains.uast.toUElement

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
            val parentElement = descriptor.psiElement.toUElement()?.uastParent
            val replacement = fixNew.replace(Config.templateString, oldText)

            val factory = JavaPsiFacade.getInstance(project).elementFactory
            val newSourceString = "{ ${parentElement?.asSourceString()?.replace(fixOld.toRegex(), replacement) ?: ""} }"

            val newCodeBlock = factory.createCodeBlockFromText(newSourceString, descriptor.psiElement.parent)
            val resultElement = parentElement?.sourcePsiElement?.replace(newCodeBlock)

            // Delete the front and end curly braces
            resultElement?.firstChild?.delete()
            resultElement?.lastChild?.delete()
        } catch (e: Exception) {
            LOG.error(e)
        }
    }
}