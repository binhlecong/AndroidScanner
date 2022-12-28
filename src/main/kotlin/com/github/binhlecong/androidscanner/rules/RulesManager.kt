package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.Config
import kotlinx.serialization.json.Json
import java.io.File

object RulesManager {
    private var JavaRules: Array<UastRule>? = null
    private var KotlinRules: Array<UastRule>? = null
    private var XmlRules: Array<XmlRule>? = null

    fun getJavaRules(): Array<UastRule> {
        if (JavaRules == null) {
            val inputStream = File(Config.PATH + "/java.json").inputStream()
            val inputString = inputStream.reader().use { it.readText() }

            val data = Json.decodeFromString(UastRuleList.serializer(), inputString.trimIndent().trim())
            JavaRules = data.rules.toTypedArray()
        }
        return JavaRules ?: emptyArray()
    }

    fun saveJavaRules(rules: ArrayList<UastRule>) {
        val jsonString = Json.encodeToString(UastRuleList.serializer(), UastRuleList(rules))
        val outputStream = File(Config.PATH + "/java.json").outputStream()
        outputStream.write(jsonString.toByteArray())
        // Todo: apply changes
    }

    fun cloneJavaRules(): ArrayList<UastRule> {
        val inputStream = File(Config.PATH + "/java.json").inputStream()
        val inputString = inputStream.reader().use { it.readText() }
        val data = Json.decodeFromString(UastRuleList.serializer(), inputString.trimIndent().trim())
        return ArrayList(data.rules)
    }

    fun getKotlinRules(): Array<UastRule> {
        if (KotlinRules == null) {
            val inputStream = File(Config.PATH + "/kotlin.json").inputStream()
            val inputString = inputStream.reader().use { it.readText() }

            val data = Json.decodeFromString(UastRuleList.serializer(), inputString.trimIndent().trim())
            KotlinRules = data.rules.toTypedArray()
        }
        return KotlinRules ?: emptyArray()
    }

    fun saveKotlinRules(rules: ArrayList<UastRule>) {
        val jsonString = Json.encodeToString(UastRuleList.serializer(), UastRuleList(rules))
        val outputStream = File(Config.PATH + "/kotlin.json").outputStream()
        outputStream.write(jsonString.toByteArray())
        // Todo: apply changes
    }

    fun cloneKotlinRules(): ArrayList<UastRule> {
        val inputStream = File(Config.PATH + "/kotlin.json").inputStream()
        val inputString = inputStream.reader().use { it.readText() }
        val data = Json.decodeFromString(UastRuleList.serializer(), inputString.trimIndent().trim())
        return ArrayList(data.rules)
    }

    fun getXmlRules(): Array<XmlRule> {
        if (XmlRules == null) {
            val inputStream = File(Config.PATH + "/xml.json").inputStream()
            val inputString = inputStream.reader().use { it.readText() }

            val data = Json.decodeFromString(UastRuleList.serializer(), inputString.trimIndent().trim())
            KotlinRules = data.rules.toTypedArray()
        }
        return XmlRules ?: emptyArray()
    }

    fun saveXmlRules(rules: ArrayList<XmlRule>) {
        val jsonString = Json.encodeToString(XmlRuleList.serializer(), XmlRuleList(rules))
        val outputStream = File(Config.PATH + "/xml.json").outputStream()
        outputStream.write(jsonString.toByteArray())
        // Todo: apply changes
    }

    fun cloneXmlRules(): ArrayList<XmlRule> {
        val inputStream = File(Config.PATH + "/xml.json").inputStream()
        val inputString = inputStream.reader().use { it.readText() }
        val data = Json.decodeFromString(XmlRuleList.serializer(), inputString.trimIndent().trim())
        return ArrayList(data.rules)
    }
}