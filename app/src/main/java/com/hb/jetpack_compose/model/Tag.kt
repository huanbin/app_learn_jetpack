package com.hb.jetpack_compose.model

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val name: String,
    val url: String
)