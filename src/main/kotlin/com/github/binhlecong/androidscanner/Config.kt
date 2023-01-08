package com.github.binhlecong.androidscanner

class Config {
    companion object {
        var PATH = ""
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
        const val RULES_URL = "https://raw.githubusercontent.com/binhlecong/rules_storage/main"
    }
}