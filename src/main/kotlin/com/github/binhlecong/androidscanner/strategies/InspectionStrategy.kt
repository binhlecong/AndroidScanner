package com.github.binhlecong.androidscanner.strategies

import com.intellij.codeInspection.LocalQuickFix

interface InspectionStrategy<T> {
    fun isSecurityIssue(node: T): Boolean
    fun buildFixes(node: T): Array<LocalQuickFix>
    override fun toString(): String
}
