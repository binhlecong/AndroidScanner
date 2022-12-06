package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.Config
import kotlinx.serialization.json.Json
import java.io.File

object RulesManager {
    private var javaRules: Array<JavaRule>? = null
    private var kotlinRules: Array<KotlinRule>? = null

    fun getJavaRules(): Array<JavaRule> {
        if (javaRules == null) {
            val inputStream = File(Config.PATH + "/java.json").inputStream()
            val inputString = inputStream.reader().use { it.readText() }

            val data = Json.decodeFromString(JavaRuleList.serializer(), inputString.trimIndent().trim())
            javaRules = data.rules.toTypedArray()
        }
        return javaRules ?: emptyArray()
    }

    fun getKotlinRules(): Array<KotlinRule> {
        if (kotlinRules == null) {
            val inputStream = File(Config.PATH + "/kotlin.json").inputStream()
            val inputString = inputStream.reader().use { it.readText() }

            val data = Json.decodeFromString(KotlinRuleList.serializer(), inputString.trimIndent().trim())
            return data.rules.toTypedArray()
        }
        return kotlinRules ?: emptyArray()
    }
}