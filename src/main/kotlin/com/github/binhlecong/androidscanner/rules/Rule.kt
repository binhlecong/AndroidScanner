package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class RuleFile(val fileName: String) {
    JAVA("java.json"),
    KOTLIN("kotlin.json"),
    XML("xml.json"),
}

@Serializable
class Inspection(
    var pattern: String,
    @SerialName("group_patterns") var groupPatterns: List<String?>,
)

@Serializable
data class Rule(
    var id: String,
    @SerialName("brief_description") var briefDescription: String,
    var inspector: Inspection,
    var fixes: List<ReplaceStrategy>,
    @SerialName("highlight_type") var highlightType: String,
    var enabled: Boolean,
)

@Serializable
data class RuleList(val rules: List<Rule>)

//@Serializable
//data class UastRule(
//    override var id: String,
//    @SerialName("brief_description") override var briefDescription: String,
//    override var inspector: UastInspectionStrategy,
//    override var fixes: List<ReplaceStrategy>,
//    @SerialName("highlight_type") override var highlightType: String,
//    override var enabled: Boolean,
//) : Rule<UastInspectionStrategy>
//
//@Serializable
//data class XmlRule(
//    override var id: String,
//    @SerialName("brief_description") override var briefDescription: String,
//    override var inspector: XmlInspectionStrategy,
//    override var fixes: List<ReplaceStrategy>,
//    @SerialName("highlight_type") override var highlightType: String,
//    override var enabled: Boolean,
//) : Rule<XmlInspectionStrategy>

//@Serializable
//data class UastRuleList(val rules: List<UastRule>)
//
//@Serializable
//data class XmlRuleList(val rules: List<XmlRule>)
