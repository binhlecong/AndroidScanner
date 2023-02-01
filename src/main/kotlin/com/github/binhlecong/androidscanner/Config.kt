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
        const val DEFAULT_RULE_URL = "https://raw.githubusercontent.com/binhlecong/rules_storage/main"
        const val PLUGIN_NAME = "9Fix2"
        const val PLUGIN_FILE_EXT = "9fix2";
        const val PLUGIN_CONFIG_DIR = PLUGIN_NAME + "Config"
        const val RULE_DATA_DIR = PLUGIN_NAME + "RuleData"
    }
}