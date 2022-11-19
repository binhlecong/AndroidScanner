package com.github.binhlecong.androidscanner.inspection_strategies

import org.jetbrains.uast.UExpression

class UastInspectionStrategy(
    private val pattern: String,
    private val groupPatterns: Array<String?>,
) : InspectionStrategy<UExpression> {
    override fun isSecurityIssue(node: UExpression): Boolean {
        val sourceString = node.asSourceString()
        val match = Regex(pattern).find(sourceString) ?: return false

        val numberedGroupValues = match.destructured.toList()

        val patternsSize = groupPatterns.size
        if (numberedGroupValues.size < patternsSize) {
            return false
        }

        for (i in 0 until patternsSize) {
            val groupPattern = groupPatterns[i] ?: continue
            if (!Regex(groupPattern).matches(numberedGroupValues[i])) {
                return false
            }
        }
        return true
    }
}