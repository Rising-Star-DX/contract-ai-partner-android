package com.poscodx.contract_ai_partner.domain.model

data class StandardDetail(
    val id: Long,
    val name: String,
    val type: String,
    val url: String,
    val status: String,
    val createdAt: String,
    val categoryName: String
)
