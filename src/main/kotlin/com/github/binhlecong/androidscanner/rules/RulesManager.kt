package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.Config
import kotlinx.serialization.json.Json
import java.io.File

class RulesManager {
    fun getJavaRules(): Array<JavaRule> {
        val inputStream = File(Config.PATH + "/java.json").inputStream()
        val inputString = inputStream.reader().use { it.readText() }
        inputString.trim()

        val data = Json.decodeFromString(JavaRuleList.serializer(), inputString.trimIndent().trim())
        return data.rules.toTypedArray()
    }

    fun getKotlinRules(): Array<KotlinRule> {
        // todo: parse rule from json
        return emptyArray()
    }
}