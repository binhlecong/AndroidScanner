package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.strategies.GradleInspectionStrategy
import com.github.binhlecong.androidscanner.strategies.JavaInspectionStrategy
import com.github.binhlecong.androidscanner.strategies.KotlinInspectionStrategy
import com.github.binhlecong.androidscanner.strategies.XmlInspectionStrategy
import com.intellij.codeInspection.ProblemHighlightType

interface Rule<T> {
    val id: String
    val briefDescription: String
    val inspector: T
    val highlightType: ProblemHighlightType
}

data class JavaRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: JavaInspectionStrategy,
    override val highlightType: ProblemHighlightType,
) : Rule<JavaInspectionStrategy>

data class KotlinRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: KotlinInspectionStrategy,
    override val highlightType: ProblemHighlightType,
) : Rule<KotlinInspectionStrategy>

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
