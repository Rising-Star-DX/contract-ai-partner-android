package com.poscodx.contract_ai_partner.domain.model


data class ContractDetail(
    val id: Long,
    val name: String,
    val type: String,
    val url: String,
    val status: String,
    val categoryName: String,
    val totalPage: Int,
    val incorrectTexts: List<IncorrectText>
)
