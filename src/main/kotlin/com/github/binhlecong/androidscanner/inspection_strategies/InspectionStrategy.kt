package com.github.binhlecong.androidscanner.inspection_strategies

import com.github.binhlecong.androidscanner.rules.Inspection

interface InspectionStrategy<T> {
    fun isSecurityIssue(node: T, inspection: Inspection): Boolean
}