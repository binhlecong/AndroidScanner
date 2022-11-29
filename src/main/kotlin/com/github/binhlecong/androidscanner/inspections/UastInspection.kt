package com.github.binhlecong.androidscanner.inspections

import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.rules.JavaRule
import com.github.binhlecong.androidscanner.rules.RuleListSerializer
import com.intellij.codeInspection.*
import com.intellij.psi.PsiFile
import kotlinx.serialization.json.Json
import org.jetbrains.uast.UFile
import org.jetbrains.uast.toUElement
import java.io.File

class UastInspection : AbstractBaseUastLocalInspectionTool(UFile::class.java) {
    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor> {
        val uFile = file.toUElement(UFile::class.java) ?: return ProblemDescriptor.EMPTY_ARRAY
        val issues = arrayListOf<ProblemDescriptor>()

        val inputStream = File(Config.PATH + "/java.json").inputStream()
        val inputString = inputStream.reader().use { it.readText() }
        inputString.trim()

        var res = arrayOf<JavaRule>()
        val data = Json.decodeFromString(RuleListSerializer.serializer(), inputString.trimIndent().trim())

        issues.add(
            manager.createProblemDescriptor(
                file,
                data.rules.size.toString(),
                isOnTheFly,
                LocalQuickFix.EMPTY_ARRAY,
                ProblemHighlightType.WARNING,
            )
        )
        return issues.toTypedArray()
    }
}