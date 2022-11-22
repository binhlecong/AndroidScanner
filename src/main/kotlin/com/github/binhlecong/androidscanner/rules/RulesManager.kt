package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.fix_strategies.ReplaceStrategy
import com.github.binhlecong.androidscanner.inspection_strategies.UastInspectionStrategy
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.LocalQuickFix
import org.json.JSONTokener
import org.json.JSONArray
import java.io.File
import android.util.Log

class RulesManager {
    fun getJavaRules(): Array<JavaRule> {
        val inputStream = File("./ruleset/java.json").inputStream()
        val inputString = inputStream.reader().use {it.readText()}
        var res = arrayOf<JavaRule>()
        val jsonArray = JSONTokener(inputString).nextValue() as JSONArray


        for (i in 0 until jsonArray.length()){
            val id = jsonArray.getJSONObject(i).getString("id")
            Log.i("id", id)

            val briefDescription = jsonArray.getJSONObject(i).getString("brief_description")
            Log.i("brief_description", briefDescription)

            var highlightType = ProblemHighlightType.INFORMATION
            when (jsonArray.getJSONObject(i).getString("highlight_type")){
                "WARNING" -> highlightType = ProblemHighlightType.WARNING
                "ERROR" -> highlightType = ProblemHighlightType.ERROR
            }
            Log.i("highlight_type", highlightType.toString())

            val inspector = jsonArray.getJSONObject(i).getJSONObject("inspector")

            val pattern = inspector.getString("pattern")
            Log.i("pattern", pattern)
            val groupPatterns = inspector.getJSONArray("group_patterns")
            val groupPatternsArray = Array(groupPatterns.length()) {
                groupPatterns.getString((it))
            }
            Log.i("group_patterns", groupPatterns.toString())
            val inspectionStrategy = UastInspectionStrategy(pattern, groupPatternsArray)

            var fixesArray = arrayOf<LocalQuickFix>()
            val fixes = inspector.getJSONArray("fixes")
            for (j in 0 until fixes.length()){
                val name = fixes.getJSONObject(j).getString("name")
                val patterns = fixes.getJSONObject(j).getJSONArray("patterns")
                val patternsArray = Array(patterns.length()) {
                    patterns.getString((it))
                }
                val strings = fixes.getJSONObject(j).getJSONArray("strings")
                val stringsArray = Array(strings.length()) {
                    strings.getString((it))
                }
                fixesArray += ReplaceStrategy(name, patternsArray, stringsArray)
            }
            Log.i("fixesArray", fixesArray.toString())


            res += JavaRule(id, briefDescription, inspectionStrategy, fixesArray, highlightType)

        }

        /* return arrayOf(
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
        ) */
        return res
    }

    fun getKotlinRules(): Array<KotlinRule> {
        val inputStream = File("./ruleset/kotlin.json").inputStream()
        val inputString = inputStream.reader().use {it.readText()}
        var res = arrayOf<JavaRule>()
        val jsonArray = JSONTokener(inputString).nextValue() as JSONArray


        for (i in 0 until jsonArray.length()){
            val id = jsonArray.getJSONObject(i).getString("id")
            Log.i("id", id)

            val briefDescription = jsonArray.getJSONObject(i).getString("brief_description")
            Log.i("brief_description", briefDescription)

            var highlightType = ProblemHighlightType.INFORMATION
            when (jsonArray.getJSONObject(i).getString("highlight_type")){
                "WARNING" -> highlightType = ProblemHighlightType.WARNING
                "ERROR" -> highlightType = ProblemHighlightType.ERROR
            }
            Log.i("highlight_type", highlightType.toString())

            val inspector = jsonArray.getJSONObject(i).getJSONObject("inspector")

            val pattern = inspector.getString("pattern")
            Log.i("pattern", pattern)
            val groupPatterns = inspector.getJSONArray("group_patterns")
            val groupPatternsArray = Array(groupPatterns.length()) {
                groupPatterns.getString((it))
            }
            Log.i("group_patterns", groupPatterns.toString())
            val inspectionStrategy = UastInspectionStrategy(pattern, groupPatternsArray)

            var fixesArray = arrayOf<LocalQuickFix>()
            val fixes = inspector.getJSONArray("fixes")
            for (j in 0 until fixes.length()){
                val name = fixes.getJSONObject(j).getString("name")
                val patterns = fixes.getJSONObject(j).getJSONArray("patterns")
                val patternsArray = Array(patterns.length()) {
                    patterns.getString((it))
                }
                val strings = fixes.getJSONObject(j).getJSONArray("strings")
                val stringsArray = Array(strings.length()) {
                    strings.getString((it))
                }
                fixesArray += ReplaceStrategy(name, patternsArray, stringsArray)
            }
            Log.i("fixesArray", fixesArray.toString())


            res += JavaRule(id, briefDescription, inspectionStrategy, fixesArray, highlightType)

        }

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