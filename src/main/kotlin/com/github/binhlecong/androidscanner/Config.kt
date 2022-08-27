package com.github.binhlecong.androidscanner

object Config {

    const val templateString = "\${oldText}"
    var PATH = ""
    const val FIELD_ID = 0
    const val FIELD_briefDescription = 1
    const val FIELD_explanation = 2
    const val FIELD_priority = 3
    const val FIELD_className = 4

    //const val FIELD_XMLTag = 4
    const val FIELD_methodName = 5
    const val FIELD_xmlAttribute = 5
    const val FIELD_paramPattern = 6
    const val FIELD_xmlAttributeValue = 6
    const val FIELD_paramIndex = 7
    const val FIELD_needFix = 8
    const val FIELD_fixName = 9
    const val FIELD_fixOld = 10
    const val FIELD_fixNew = 11
    const val FIELD_type = 12
    const val TYPE_METHOD = "METHOD"
    const val TYPE_METHOD_PARAM = "METHOD_PARAM"
    const val TYPE_CONSTRUCTOR = "CONSTRUCTOR"
    const val TYPE_CONSTRUCTOR_PARAM = "CONSTRUCTOR_PARAM"
    const val TYPE_XML_ATTRIBUTE = "XMLATTRIBUTE"
    val FIELD_NAMES = arrayOf(
        "ID",
        "briefDescription",
        "explanation",
        "priority",
        "className",
        "methodName",
        "paramPattern",
        "paramIndex",
        "needFix",
        "fixName",
        "fixOld",
        "fixNew",
        "type",
    )
    const val ACTION_SIZE_WIDTH = 1000
    const val ACTION_SIZE_HEIGHT = 500
    const val RULES_URL = "https://raw.githubusercontent.com/nguyenquan9699/9fix/main/Rules.xml"

}