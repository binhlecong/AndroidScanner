package com.github.binhlecong.androidscanner.inspection_strategies

import com.intellij.codeInspection.LocalQuickFix

interface InspectionStrategy<T> {
    fun isSecurityIssue(node: T): Boolean
    fun buildFixes(node: T): Array<LocalQuickFix>
}