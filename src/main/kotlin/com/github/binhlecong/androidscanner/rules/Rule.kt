package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.inspection_strategies.GradleInspectionStrategy
import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy
import com.github.binhlecong.androidscanner.inspection_strategies.XmlInspectionStrategy
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemHighlightType

interface Rule<T> {
    val id: String
    val briefDescription: String
    val inspector: T
    val fixes: Array<LocalQuickFix>
    val highlightType: ProblemHighlightType
}

data class JavaRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: UastInspectionStrategy,
    override val fixes: Array<LocalQuickFix>,
    override val highlightType: ProblemHighlightType,
) : Rule<UastInspectionStrategy> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaRule

        if (id != other.id) return false
        if (briefDescription != other.briefDescription) return false
        if (inspector != other.inspector) return false
        if (!fixes.contentEquals(other.fixes)) return false
        if (highlightType != other.highlightType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + briefDescription.hashCode()
        result = 31 * result + inspector.hashCode()
        result = 31 * result + fixes.contentHashCode()
        result = 31 * result + highlightType.hashCode()
        return result
    }
}

data class KotlinRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: UastInspectionStrategy,
    override val fixes: Array<LocalQuickFix>,
    override val highlightType: ProblemHighlightType,
) : Rule<UastInspectionStrategy> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KotlinRule

        if (id != other.id) return false
        if (briefDescription != other.briefDescription) return false
        if (inspector != other.inspector) return false
        if (!fixes.contentEquals(other.fixes)) return false
        if (highlightType != other.highlightType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + briefDescription.hashCode()
        result = 31 * result + inspector.hashCode()
        result = 31 * result + fixes.contentHashCode()
        result = 31 * result + highlightType.hashCode()
        return result
    }
}

data class XmlRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: XmlInspectionStrategy,
    override val fixes: Array<LocalQuickFix>,
    override val highlightType: ProblemHighlightType,
) : Rule<XmlInspectionStrategy> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as XmlRule

        if (id != other.id) return false
        if (briefDescription != other.briefDescription) return false
        if (inspector != other.inspector) return false
        if (!fixes.contentEquals(other.fixes)) return false
        if (highlightType != other.highlightType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + briefDescription.hashCode()
        result = 31 * result + inspector.hashCode()
        result = 31 * result + fixes.contentHashCode()
        result = 31 * result + highlightType.hashCode()
        return result
    }
}

data class GradleRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: GradleInspectionStrategy,
    override val fixes: Array<LocalQuickFix>,
    override val highlightType: ProblemHighlightType,
) : Rule<GradleInspectionStrategy> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GradleRule

        if (id != other.id) return false
        if (briefDescription != other.briefDescription) return false
        if (inspector != other.inspector) return false
        if (!fixes.contentEquals(other.fixes)) return false
        if (highlightType != other.highlightType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + briefDescription.hashCode()
        result = 31 * result + inspector.hashCode()
        result = 31 * result + fixes.contentHashCode()
        result = 31 * result + highlightType.hashCode()
        return result
    }
}
