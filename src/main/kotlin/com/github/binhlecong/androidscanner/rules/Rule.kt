package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy
import com.github.binhlecong.androidscanner.inspection_strategies.GradleInspectionStrategy
import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy
import com.github.binhlecong.androidscanner.inspection_strategies.XmlInspectionStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Rule<T> {
    var id: String
    var briefDescription: String
    var inspector: T
    var fixes: List<ReplaceStrategy>
    var highlightType: String
    var enabled: Boolean
}

@Serializable
data class JavaRule(
    override var id: String,
    @SerialName("brief_description") override var briefDescription: String,
    override var inspector: UastInspectionStrategy,
    override var fixes: List<ReplaceStrategy>,
    @SerialName("highlight_type") override var highlightType: String,
    override var enabled: Boolean,
) : Rule<UastInspectionStrategy>

@Serializable
data class KotlinRule(
    override var id: String,
    @SerialName("brief_description") override var briefDescription: String,
    override var inspector: UastInspectionStrategy,
    override var fixes: List<ReplaceStrategy>,
    @SerialName("highlight_type") override var highlightType: String,
    override var enabled: Boolean,
) : Rule<UastInspectionStrategy>

data class XmlRule(
    override var id: String,
    override var briefDescription: String,
    override var inspector: XmlInspectionStrategy,
    override var fixes: List<ReplaceStrategy>,
    override var highlightType: String,
    override var enabled: Boolean,
) : Rule<XmlInspectionStrategy>

data class GradleRule(
    override var id: String,
    override var briefDescription: String,
    override var inspector: GradleInspectionStrategy,
    override var fixes: List<ReplaceStrategy>,
    override var highlightType: String,
    override var enabled: Boolean,
) : Rule<GradleInspectionStrategy>

@Serializable
data class JavaRuleList(val rules: List<JavaRule>)

@Serializable
data class KotlinRuleList(val rules: List<KotlinRule>)