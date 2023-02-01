package com.github.binhlecong.androidscanner.inspection_strategies

import com.github.binhlecong.androidscanner.rules.Inspection
import com.intellij.psi.xml.XmlAttribute

object XmlInspectionStrategy : InspectionStrategy<XmlAttribute> {
    override fun isSecurityIssue(node: XmlAttribute, inspection: Inspection): Boolean {
        val pattern = inspection.pattern
        val groupPatterns = inspection.groupPatterns

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
