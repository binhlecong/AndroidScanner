package com.github.binhlecong.androidscanner.rules

interface Rule {
    val id: String
    val briefDescription: String
}

data class UastRule(override val id: String, override val briefDescription: String) : Rule
data class XmlRule(override val id: String, override val briefDescription: String) : Rule
data class GradleRule(override val id: String, override val briefDescription: String) : Rule
