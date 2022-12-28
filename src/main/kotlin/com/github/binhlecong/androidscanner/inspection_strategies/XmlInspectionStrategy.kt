package com.github.binhlecong.androidscanner.inspection_strategies

import com.intellij.psi.xml.XmlAttribute
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class XmlInspectionStrategy(
    private var pattern: String,
    @SerialName("group_patterns") var groupPatterns: List<String?>,
) : InspectionStrategy<XmlAttribute> {
    override fun isSecurityIssue(node: XmlAttribute): Boolean {
        val sourceString = node.text
        val match = Regex(pattern).find(sourceString) ?: return false

        val numberedGroupValues = match.destructured.toList()

        val patternsSize = groupPatterns.size
        if (numberedGroupValues.size < patternsSize) {
            return false
        }

        for (i in 0 until patternsSize) {
            val groupPattern = groupPatterns[i] ?: continue
            if (!Regex(groupPattern).matches(numberedGroupValues[i])) {
                return false
            }
        }

        return true
    }
}
