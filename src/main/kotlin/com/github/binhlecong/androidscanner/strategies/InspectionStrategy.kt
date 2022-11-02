package com.github.binhlecong.androidscanner.strategies

import com.intellij.codeInspection.LocalQuickFix

interface InspectionStrategy {
    fun isSecurityIssue(): Boolean;
    fun buildFixes(): LocalQuickFix;
}
