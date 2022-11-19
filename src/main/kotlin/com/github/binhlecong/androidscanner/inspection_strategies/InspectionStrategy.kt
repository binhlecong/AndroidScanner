package com.github.binhlecong.androidscanner.inspection_strategies

interface InspectionStrategy<T> {
    fun isSecurityIssue(node: T): Boolean
}