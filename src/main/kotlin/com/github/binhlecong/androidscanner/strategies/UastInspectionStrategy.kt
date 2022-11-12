package com.github.binhlecong.androidscanner.strategies

import com.intellij.codeInspection.LocalQuickFix
import org.jetbrains.uast.UCallExpression


interface UastInspectionStrategy : InspectionStrategy<UCallExpression>

class CallNameInspectionStrategy(private val callPattern: String) : UastInspectionStrategy {
    override fun isSecurityIssue(node: UCallExpression): Boolean {
        val sourceString = node.asSourceString()
        // ex: MessageDigest.getInstance(x)
        val callName = sourceString.split('(').first()
        if (callName == callPattern)
            return true
        return false
    }

    override fun buildFixes(node: UCallExpression): Array<LocalQuickFix> {
        return LocalQuickFix.EMPTY_ARRAY
    }

    override fun toString(): String {
        return "demo expression inspection: name"
    }
}

class ArgumentInspectionStrategy(
    private val callPattern: String,
    private val argIndex: Int,
    private val argPattern: String,
) :
    UastInspectionStrategy {
    override fun isSecurityIssue(node: UCallExpression): Boolean {
        val sourceString = node.asSourceString()
        // ex: MessageDigest.getInstance(x)
        val callName = sourceString.split('(').first()
        if (callName == callPattern && node.valueArgumentCount > argIndex) {
            val argument = node.getArgumentForParameter(argIndex)?.sourcePsi?.text
            if (argument == argPattern)
                return true
        }
        return false
    }

    override fun buildFixes(node: UCallExpression): Array<LocalQuickFix> {
        return LocalQuickFix.EMPTY_ARRAY
    }

    override fun toString(): String {
        return "demo expression inspection: param"
    }
}