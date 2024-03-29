package com.github.binhlecong.androidscanner.fix_strategies

import com.github.binhlecong.androidscanner.Config
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.rpc.LOG
import kotlin.math.min

@Serializable
class ReplaceStrategy(
    @SerialName("name") var fixName: String,
    var patterns: List<String>,
    var strings: List<String>,
) : LocalQuickFix {

    override fun getFamilyName(): String {
        return Config.PLUGIN_NAME + ": " + fixName
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        try {
            var sourceString = descriptor.psiElement.text
            val textRange = descriptor.psiElement.textRange

            val editor = FileEditorManager.getInstance(project).selectedTextEditor
            val document = editor?.document ?: return

            val n = min(patterns.size, strings.size)
            for (i in 0 until n) {
                val regex = patterns[i].toRegex()
                sourceString = regex.replaceFirst(sourceString, strings[i])
            }

            document.replaceString(textRange.startOffset, textRange.endOffset, sourceString)
        } catch (e: Exception) {
            LOG.error(e)
        }
    }
}