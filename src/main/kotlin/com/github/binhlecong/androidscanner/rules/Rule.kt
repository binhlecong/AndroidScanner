package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.strategies.InspectionStrategy
import com.intellij.codeInspection.ProblemHighlightType

interface Rule {
    val id: String
    val briefDescription: String
    val inspector: InspectionStrategy
    val highlightType: ProblemHighlightType
}

data class UastRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: InspectionStrategy,
    override val highlightType: ProblemHighlightType,
    val position: String,
    val matcher: String,
) : Rule

data class XmlRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: InspectionStrategy,
    override val highlightType: ProblemHighlightType,
) : Rule

data class GradleRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: InspectionStrategy,
    override val highlightType: ProblemHighlightType,
) : Rule
