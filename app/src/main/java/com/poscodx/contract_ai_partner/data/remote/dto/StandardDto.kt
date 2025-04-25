package com.poscodx.contract_ai_partner.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StandardDto(
    val id: Long,
    val name: String,
    val type: String,
    val status: String,
    val createdAt: String,
    val categoryName: String
)
