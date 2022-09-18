package com.hb.jetpack_compose.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val name: String,
    val url: String
)