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
            val node = descriptor.psiElement.toUElement()
            val parentNode = node?.uastParent
            val newSource = fixNew.replace(Config.templateString, oldText)
            val newParentSource = "{ ${parentNode?.asSourceString()?.replace(fixOld.toRegex(), newSource) ?: ""} }"
            val factory = JavaPsiFacade.getInstance(project).elementFactory
            val newExpression = factory.createCodeBlockFromText(newParentSource, descriptor.psiElement.parent)
            parentNode?.sourcePsiElement?.replace(newExpression)
        } catch (e: Exception) {
            LOG.error(e)
        }
    }
}