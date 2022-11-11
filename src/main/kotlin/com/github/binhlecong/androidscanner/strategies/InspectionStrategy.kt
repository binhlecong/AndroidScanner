package com.github.binhlecong.androidscanner.strategies

import com.intellij.codeInspection.LocalQuickFix

interface InspectionStrategy {
    fun isSecurityIssue(sourceStr: String): Boolean
    fun buildFixes(): LocalQuickFix
    override fun toString(): String
}
