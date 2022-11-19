package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy
import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy
import com.intellij.codeInspection.ProblemHighlightType

class RulesManager {
    fun getJavaRules(): Array<JavaRule> {
        // todo: parse rule from json
        return arrayOf(
            JavaRule(
                "PredictableRandom",
                "Predictable pseudorandom number generator",
                UastInspectionStrategy(
                    "new Random()",
                    emptyArray(),
                ),
                arrayOf(
                    ReplaceStrategy(
                        "Use SecureRandom",
                        arrayOf("Random"),
                        arrayOf("SecureRandom"),
                    ),
                ),
                ProblemHighlightType.WARNING,
            ),
            JavaRule(
                "MessageDigest",
                "SHA-1 and Message-Digest hash algorithms should not be used in secure contexts",
                UastInspectionStrategy(
                    "MessageDigest.getInstance\\((.*?)\\)",
                    arrayOf("\"SHA1\""),
                ),
                arrayOf(
                    ReplaceStrategy(
                        "Improve security by using SHA256",
                        arrayOf("SHA1"),
                        arrayOf("SHA256"),
                    ),
                ),
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
                UastInspectionStrategy(
                    "Random",
                    emptyArray(),
                ),
                arrayOf(
                    ReplaceStrategy(
                        "Use SecureRandom",
                        arrayOf("Random"),
                        arrayOf("SecureRandom"),
                    ),
                ),
                ProblemHighlightType.WARNING,
            ),
            KotlinRule(
                "MessageDigest",
                "SHA-1 and Message-Digest hash algorithms should not be used in secure contexts",
                UastInspectionStrategy(
                    "MessageDigest.getInstance\\((.*?)\\)",
                    arrayOf("\"SHA1\""),
                ),
                arrayOf(
                    ReplaceStrategy(
                        "Improve security by using SHA256",
                        arrayOf("SHA1"), arrayOf("SHA256"),
                    ),
                ),
                ProblemHighlightType.WARNING,
            )
        )
    }
}