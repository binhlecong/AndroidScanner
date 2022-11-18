package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy
import com.intellij.codeInspection.ProblemHighlightType

class RulesManager {
    fun getJavaRules(): Array<JavaRule> {
        // todo: parse rule from json
        return arrayOf(
            JavaRule(
                "PredictableRandom",
                "Predictable pseudorandom number generator",
                UastInspectionStrategy("new Random()", emptyArray()),
                ProblemHighlightType.WARNING,
            ),
            JavaRule(
                "MessageDigest",
                "SHA-1 and Message-Digest hash algorithms should not be used in secure contexts",
                UastInspectionStrategy("MessageDigest.getInstance\\((.*?)\\)", arrayOf("\"SHA1\"")),
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
                UastInspectionStrategy("Random", emptyArray()),
                ProblemHighlightType.WARNING,
            ),
            KotlinRule(
                "MessageDigest",
                "SHA-1 and Message-Digest hash algorithms should not be used in secure contexts",
                UastInspectionStrategy("MessageDigest.getInstance\\((.*?)\\)", arrayOf("\"SHA1\"")),
                ProblemHighlightType.WARNING,
            )
        )
    }
}