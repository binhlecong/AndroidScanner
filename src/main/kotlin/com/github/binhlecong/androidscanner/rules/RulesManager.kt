package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.strategies.ArgumentInspectionStrategy
import com.github.binhlecong.androidscanner.strategies.CallNameInspectionStrategy
import com.intellij.codeInspection.ProblemHighlightType

class RulesManager {
    fun getUastRules(query: String): Array<UastRule> {
        // fixme: just a demo
        return arrayOf(
            UastRule(
                "PredictableRandom",
                "Predictable pseudorandom number generator",
                CallNameInspectionStrategy("Random"),
                ProblemHighlightType.WARNING,
            ),
            UastRule(
                "MessageDigest",
                "SHA-1 and Message-Digest hash algorithms should not be used in secure contexts",
                ArgumentInspectionStrategy("MessageDigest.getInstance", 0, "SHA1"),
                ProblemHighlightType.WARNING,
            )
        )
    }
}