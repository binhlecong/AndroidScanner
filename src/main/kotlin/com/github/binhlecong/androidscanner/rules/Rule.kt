package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.strategies.GradleInspectionStrategy
import com.github.binhlecong.androidscanner.strategies.UastInspectionStrategy
import com.github.binhlecong.androidscanner.strategies.XmlInspectionStrategy
import com.intellij.codeInspection.ProblemHighlightType

interface Rule<T> {
    val id: String
    val briefDescription: String
    val inspector: T
    val highlightType: ProblemHighlightType
}

data class UastRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: UastInspectionStrategy,
    override val highlightType: ProblemHighlightType,
) : Rule<UastInspectionStrategy>

data class XmlRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: XmlInspectionStrategy,
    override val highlightType: ProblemHighlightType,
) : Rule<XmlInspectionStrategy>

data class GradleRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: GradleInspectionStrategy,
    override val highlightType: ProblemHighlightType,
) : Rule<GradleInspectionStrategy>
