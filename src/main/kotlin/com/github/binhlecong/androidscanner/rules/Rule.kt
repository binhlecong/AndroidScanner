package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy
import com.github.binhlecong.androidscanner.inspection_strategies.GradleInspectionStrategy
import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy
import com.github.binhlecong.androidscanner.inspection_strategies.XmlInspectionStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Rule<T> {
    val id: String
    val briefDescription: String
    val inspector: T
    val fixes: List<ReplaceStrategy>
    val highlightType: String
}

@Serializable
data class JavaRule(
    override val id: String,
    @SerialName("brief_description") override val briefDescription: String,
    override val inspector: UastInspectionStrategy,
    override val fixes: List<ReplaceStrategy>,
    @SerialName("highlight_type") override val highlightType: String,
) : Rule<UastInspectionStrategy>

data class KotlinRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: UastInspectionStrategy,
    override val fixes: List<ReplaceStrategy>,
    override val highlightType: String,
) : Rule<UastInspectionStrategy>

data class XmlRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: XmlInspectionStrategy,
    override val fixes: List<ReplaceStrategy>,
    override val highlightType: String,
) : Rule<XmlInspectionStrategy>

data class GradleRule(
    override val id: String,
    override val briefDescription: String,
    override val inspector: GradleInspectionStrategy,
    override val fixes: List<ReplaceStrategy>,
    override val highlightType: String,
) : Rule<GradleInspectionStrategy>

@Serializable
data class JavaRuleList(val rules: List<JavaRule>)