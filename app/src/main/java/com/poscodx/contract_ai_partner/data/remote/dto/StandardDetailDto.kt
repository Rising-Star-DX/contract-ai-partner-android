package com.poscodx.contract_ai_partner.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StandardDetailDto(
    val id: Long,
    val name: String,
    val type: String,
    val url: String,
    val status: String,
    val createdAt: String,
    val categoryName: String
)