package com.github.binhlecong.androidscanner.strategies

import com.intellij.codeInspection.LocalQuickFix
import org.jetbrains.uast.UExpression

interface InspectionStrategy<T> {
    fun isSecurityIssue(node: T): Boolean
    fun buildFixes(node: T): Array<LocalQuickFix>
    override fun toString(): String
}

class UastInspectionStrategy : InspectionStrategy<UExpression> {
    override fun isSecurityIssue(node: UExpression): Boolean {
        return true
    }

    override fun buildFixes(node: UExpression): Array<LocalQuickFix> {
        return LocalQuickFix.EMPTY_ARRAY
    }

    override fun toString(): String {
        return "demo expression inspection"
    }
}