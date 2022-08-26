package com.github.binhlecong.androidscanner

class Config {
    companion object {
        const val templateString = "\${oldText}"
        var PATH = ""
        const val FIELD_ID = 0
        const val FIELD_BRIEF_DESCRIPTION = 1
        const val FIELD_EXPLANATION = 2
        const val FIELD_PRIORITY = 3
        const val FIELD_CLASS_NAME = 4
        const val FIELD_XML_TAG = 4
        const val FIELD_METHOD_NAME = 5
        const val FIELD_XML_ATTRIBUTE = 5
        const val FIELD_PARAM_PATTERN = 6
        const val FIELD_XML_ATTRIBUTE_VALUE = 6
        const val FIELD_PARAM_INDEX = 7
        const val FIELD_NEED_FIX = 8
        const val FIELD_FIX_NAME = 9
        const val FIELD_FIX_OLD = 10
        const val FIELD_FIX_NEW = 11
        const val FIELD_TYPE = 12
        const val TYPE_METHOD = "0"
        const val TYPE_METHOD_PARAM = "1"
        const val TYPE_CONSTRUCTOR = "2"
        const val TYPE_CONSTRUCTOR_PARAM = "3"
        const val TYPE_XM_ATTRIBUTE = "4"
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
}