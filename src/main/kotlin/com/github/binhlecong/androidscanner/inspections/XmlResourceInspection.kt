package com.github.binhlecong.androidscanner.inspections

import com.android.tools.idea.lint.common.AndroidLintInspectionBase
import com.android.tools.idea.lint.common.LintIdeIssueRegistry
import com.android.tools.lint.detector.api.*
import com.github.binhlecong.androidscanner.Config
import com.github.binhlecong.androidscanner.Helper
import org.w3c.dom.Attr
import java.util.*

val XmlAttributeIssue: Issue = Issue
    .create(
        id = "XmlResourceIssueEntry",
        briefDescription = "",
        explanation = "",
        category = Category.CORRECTNESS,
        priority = 0,
        severity = Severity.WARNING,
        androidSpecific = true,
        implementation = Implementation(
            XmlAttributeIssueDetector::class.java,
            Scope.RESOURCE_FILE_SCOPE
        )
    )

class XmlResourceInspection : AndroidLintInspectionBase("Xml Resource Inspection", XmlAttributeIssue) {

    init {
        val registry = LintIdeIssueRegistry()
        val myIssue = registry.getIssue(XmlAttributeIssue.id)
        if (myIssue == null) {
            val list = registry.issues as MutableList<Issue>
            list.add(XmlAttributeIssue)
        }
    }

    override fun getShortName(): String {
        return "XmlResourceInspection"
    }
}

class XmlAttributeIssueDetector : Detector() {
    private val rules = Helper.loadRules(Config.PATH, Config.TYPE_XML_ATTRIBUTE)

    override fun getApplicableAttributes(): Collection<String> {
        return Helper.getField(rules, Config.FIELD_xmlAttribute)
    }

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        //context.report(XMLAttributeIssue, context.getValueLocation(attribute), "Key name:"+attribute.name.split(":")[-1])
        for (rule in rules) {
            if (attribute.name.split(':').last() == rule[Config.FIELD_xmlAttribute]) {
                if (attribute.value == rule[Config.FIELD_xmlAttributeValue]) {
                    if (rule[Config.FIELD_needFix].trim() != "1") {
                        context.report(
                            createXmlResourceIssue(rule, this),
                            context.getValueLocation(attribute),
                            rule[Config.FIELD_ID]
                        )
                    } else {
                        context.report(
                            createXmlResourceIssue(rule, this),
                            context.getValueLocation(attribute),
                            rule[Config.FIELD_ID],
                            createXmlAttribute(context, attribute, rule)
                        )
                    }
                }
            }
        }
    }

    private fun createXmlAttribute(context: XmlContext, attribute: Attr, rule: List<String>): LintFix? {
        return LintFix.create()
            .name(rule[Config.FIELD_fixName])
            .replace()
            .range(context.getValueLocation(attribute))
            .pattern(rule[Config.FIELD_fixOld])
            .with(rule[Config.FIELD_fixNew])
            .autoFix()
            .reformat(true)
            .build()
    }

    private fun createXmlResourceIssue(rule: List<String>, detector: Detector): Issue {
        return Issue.create(
            id = rule[Config.FIELD_ID],
            briefDescription = rule[Config.FIELD_briefDescription],
            explanation = rule[Config.FIELD_explanation],
            category = Category.CORRECTNESS,
            priority = rule[Config.FIELD_priority].toInt(),
            severity = Severity.WARNING,
            androidSpecific = true,
            implementation = Implementation(
                detector::class.java,
                EnumSet.of(Scope.RESOURCE_FILE)
            )
        )
    }
}