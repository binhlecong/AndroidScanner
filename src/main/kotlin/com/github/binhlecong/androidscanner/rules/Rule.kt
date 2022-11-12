package com.github.binhlecong.androidscanner.rules

import com.android.tools.idea.gradle.dsl.parser.elements.GradleDslElement
import com.github.binhlecong.androidscanner.strategies.InspectionStrategy
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.psi.xml.XmlAttribute
import org.jetbrains.uast.UExpression

interface Rule<T> {
    val id: String
    val briefDescription: String
    val inspector: InspectionStrategy<T>
    val highlightType: ProblemHighlightType
}

data class UastRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: InspectionStrategy<UExpression>,
    override val highlightType: ProblemHighlightType,
    val position: String,
    val matcher: String,
) : Rule<UExpression>

data class XmlRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: InspectionStrategy<XmlAttribute>,
    override val highlightType: ProblemHighlightType,
) : Rule<XmlAttribute>

data class GradleRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: InspectionStrategy<GradleDslElement>,
    override val highlightType: ProblemHighlightType,
) : Rule<GradleDslElement>
