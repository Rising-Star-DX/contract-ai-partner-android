package com.poscodx.contract_ai_partner.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ContractDto(
    val id: Long,
    val name: String,
    val type: String,
    val status: String,
    val createdAt: String,      // ISO8601 → 가공 필요
    val categoryName: String
)
