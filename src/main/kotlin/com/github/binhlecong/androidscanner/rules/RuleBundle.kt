package com.github.binhlecong.androidscanner.rules

import kotlinx.serialization.Serializable

@Serializable
data class RuleBundle(
    val javaRules: RuleList,
    val kotlinRules: RuleList,
    val xmlRules: RuleList,
)