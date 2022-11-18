package com.github.binhlecong.androidscanner.strategies

import com.intellij.codeInspection.LocalQuickFix
import org.jetbrains.uast.UExpression


class KotlinInspectionStrategy(private val callPattern: String) : InspectionStrategy<UExpression> {
    override fun isSecurityIssue(node: UExpression): Boolean {
        val sourceString = node.asSourceString()
        // ex: new MessageDigest.getInstance(x)
        if (sourceString == callPattern)
            return true
        return false
    }

    override fun buildFixes(node: UExpression): Array<LocalQuickFix> {
        return LocalQuickFix.EMPTY_ARRAY
    }
}