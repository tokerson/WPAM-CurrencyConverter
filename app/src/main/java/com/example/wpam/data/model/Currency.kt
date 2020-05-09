package com.example.wpam.data.model

data class Currency(
    val name: String,
    val shortName: String
) {
    override fun toString(): String = "( $shortName ) $name"
}