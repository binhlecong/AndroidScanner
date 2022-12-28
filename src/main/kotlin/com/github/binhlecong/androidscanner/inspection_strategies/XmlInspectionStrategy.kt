package com.github.binhlecong.androidscanner.inspection_strategies

import com.intellij.psi.xml.XmlAttribute
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class XmlInspectionStrategy(
    private var pattern: String,
    @SerialName("group_patterns") var groupPatterns: List<String?>,
) : InspectionStrategy<XmlAttribute> {
    override fun isSecurityIssue(attribute: XmlAttribute): Boolean {

        return true
    }
}
