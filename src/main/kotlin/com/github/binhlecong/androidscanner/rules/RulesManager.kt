package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.strategies.JavaInspectionStrategy
import com.github.binhlecong.androidscanner.strategies.KotlinInspectionStrategy
import com.intellij.codeInspection.ProblemHighlightType

class RulesManager {
    fun getJavaRules(): Array<JavaRule> {
        // fixme: just a demo
        return arrayOf(
            JavaRule(
                "PredictableRandom",
                "Predictable pseudorandom number generator",
                JavaInspectionStrategy("Random"),
                ProblemHighlightType.WARNING,
            ),
            JavaRule(
                "MessageDigest",
                "SHA-1 and Message-Digest hash algorithms should not be used in secure contexts",
                JavaInspectionStrategy("MessageDigest.getInstance"),
                ProblemHighlightType.WARNING,
            )
        )
    }

    fun getKotlinRules(): Array<KotlinRule> {
        // fixme: just a demo
        return arrayOf(
            KotlinRule(
                "PredictableRandom",
                "Predictable pseudorandom number generator",
                KotlinInspectionStrategy("Random"),
                ProblemHighlightType.WARNING,
            ),
            KotlinRule(
                "MessageDigest",
                "SHA-1 and Message-Digest hash algorithms should not be used in secure contexts",
                KotlinInspectionStrategy("MessageDigest.getInstance"),
                ProblemHighlightType.WARNING,
            )
        )
    }
}