package com.github.binhlecong.androidscanner.rules

import com.github.binhlecong.androidscanner.Config
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.project.Project
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream

object RulesManager {
    private var JavaRules: Array<Rule>? = null
    private var KotlinRules: Array<Rule>? = null
    private var XmlRules: Array<Rule>? = null

    fun getJavaRules(): Array<Rule> {
        if (JavaRules == null) {
            val inputStream = File(Config.PATH + "/${RuleFile.JAVA.fileName}").inputStream()
            val inputString = inputStream.reader().use { it.readText() }
            val data = Json.decodeFromString(RuleList.serializer(), inputString.trimIndent().trim())
            JavaRules = data.rules.toTypedArray()
        }
        return JavaRules ?: emptyArray()
    }

    fun saveJavaRules(rules: ArrayList<Rule>) {
        val jsonString = Json.encodeToString(RuleList.serializer(), RuleList(rules))
        val outputStream = File(Config.PATH + "/${RuleFile.JAVA.fileName}").outputStream()
        outputStream.write(jsonString.toByteArray())
    }

    fun cloneJavaRules(): ArrayList<Rule> {
        val inputStream = File(Config.PATH + "/${RuleFile.JAVA.fileName}").inputStream()
        val inputString = inputStream.reader().use { it.readText() }
        val data = Json.decodeFromString(RuleList.serializer(), inputString.trimIndent().trim())
        return ArrayList(data.rules)
    }

    fun getKotlinRules(): Array<Rule> {
        if (KotlinRules == null) {
            val inputStream = File(Config.PATH + "/${RuleFile.KOTLIN.fileName}").inputStream()
            val inputString = inputStream.reader().use { it.readText() }
            val data = Json.decodeFromString(RuleList.serializer(), inputString.trimIndent().trim())
            KotlinRules = data.rules.toTypedArray()
        }
        return KotlinRules ?: emptyArray()
    }

    fun saveKotlinRules(rules: ArrayList<Rule>) {
        val jsonString = Json.encodeToString(RuleList.serializer(), RuleList(rules))
        val outputStream = File(Config.PATH + "/${RuleFile.KOTLIN.fileName}").outputStream()
        outputStream.write(jsonString.toByteArray())
    }

    fun cloneKotlinRules(): ArrayList<Rule> {
        val inputStream = File(Config.PATH + "/${RuleFile.KOTLIN.fileName}").inputStream()
        val inputString = inputStream.reader().use { it.readText() }
        val data = Json.decodeFromString(RuleList.serializer(), inputString.trimIndent().trim())
        return ArrayList(data.rules)
    }

    fun getXmlRules(): Array<Rule> {
        if (XmlRules == null) {
            val inputStream = File(Config.PATH + "/${RuleFile.XML.fileName}").inputStream()
            val inputString = inputStream.reader().use { it.readText() }
            val data = Json.decodeFromString(RuleList.serializer(), inputString.trimIndent().trim())
            XmlRules = data.rules.toTypedArray()
        }
        return XmlRules ?: emptyArray()
    }

    fun saveXmlRules(rules: ArrayList<Rule>) {
        val jsonString = Json.encodeToString(RuleList.serializer(), RuleList(rules))
        val outputStream = File(Config.PATH + "/${RuleFile.XML.fileName}").outputStream()
        outputStream.write(jsonString.toByteArray())
    }

    fun cloneXmlRules(): ArrayList<Rule> {
        val inputStream = File(Config.PATH + "/${RuleFile.XML.fileName}").inputStream()
        val inputString = inputStream.reader().use { it.readText() }
        val data = Json.decodeFromString(RuleList.serializer(), inputString.trimIndent().trim())
        return ArrayList(data.rules)
    }

    fun updateRules(ruleFile: RuleFile, project: Project): Boolean {
        try {
            val inputStream = File(Config.PATH + "/${ruleFile.fileName}").inputStream()
            val inputString = inputStream.reader().use { it.readText() }
            val data = Json.decodeFromString(RuleList.serializer(), inputString.trimIndent().trim())
            when (ruleFile) {
                RuleFile.JAVA -> {
                    JavaRules = data.rules.toTypedArray()
                }

                RuleFile.KOTLIN -> {
                    KotlinRules = data.rules.toTypedArray()
                }

                RuleFile.XML -> {
                    XmlRules = data.rules.toTypedArray()
                }
            }
            // Apply changes after save rule to .json files
            DaemonCodeAnalyzer.getInstance(project).restart()
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun exportCustomRules(path: String) {
        val javaRulesData = getRuleList(RuleFile.JAVA)
        val kotlinRulesData = getRuleList(RuleFile.KOTLIN)
        val xmlRulesData = getRuleList(RuleFile.XML)
        val ruleBundle = RuleBundle(
            javaRules = javaRulesData,
            kotlinRules = kotlinRulesData,
            xmlRules = xmlRulesData,
        )
        val jsonString = Json.encodeToString(RuleBundle.serializer(), ruleBundle)
        val outputStream = File(path).outputStream()
        outputStream.write(jsonString.trimIndent().toByteArray())
    }

    private fun getRuleList(ruleFile: RuleFile): RuleList {
        val inputStream = File(Config.PATH + "/${ruleFile.fileName}").inputStream()
        val inputString = inputStream.reader().use { it.readText() }
        return Json.decodeFromString(RuleList.serializer(), inputString.trimIndent().trim())
    }

    fun importCustomRules(fileInputStream: InputStream) {
        val inputString = fileInputStream.reader().use { it.readText() }
        val ruleBundle = Json.decodeFromString(RuleBundle.serializer(), inputString.trimIndent().trim())
        saveJavaRules(ArrayList(ruleBundle.javaRules.rules))
        saveKotlinRules(ArrayList(ruleBundle.kotlinRules.rules))
        saveXmlRules(ArrayList(ruleBundle.xmlRules.rules))
    }
}