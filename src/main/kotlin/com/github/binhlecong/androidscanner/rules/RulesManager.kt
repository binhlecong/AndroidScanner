package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.inspection_strategies.JavaInspectionStrategy
import com.github.binhlecong.androidscanner.inspection_strategies.KotlinInspectionStrategy
import com.intellij.codeInspection.ProblemHighlightType

class RulesManager {
    fun getJavaRules(): Array<JavaRule> {
        // todo: parse rule from json
        return arrayOf(
            JavaRule(
                "PredictableRandom",
                "Predictable pseudorandom number generator",
                JavaInspectionStrategy("new Random()", emptyArray()),
                ProblemHighlightType.WARNING,
            ),
            JavaRule(
                "MessageDigest",
                "SHA-1 and Message-Digest hash algorithms should not be used in secure contexts",
                JavaInspectionStrategy("MessageDigest.getInstance\\((.*?)\\)", arrayOf("\"SHA1\"")),
                ProblemHighlightType.WARNING,
            )
        )
    }

    fun getKotlinRules(): Array<KotlinRule> {
        // todo: parse rule from json
        return arrayOf(
            KotlinRule(
                "PredictableRandom",
                "Predictable pseudorandom number generator",
                KotlinInspectionStrategy("Random", emptyArray()),
                ProblemHighlightType.WARNING,
            ),
            KotlinRule(
                "MessageDigest",
                "SHA-1 and Message-Digest hash algorithms should not be used in secure contexts",
                KotlinInspectionStrategy("MessageDigest.getInstance\\((.*?)\\)", arrayOf("\"SHA1\"")),
                ProblemHighlightType.WARNING,
            )
        )
    }
}