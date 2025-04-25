package com.poscodx.contract_ai_partner.domain.model

import java.time.LocalDateTime

data class Standard(
    val id: Long,
    val fileName: String,
    val fileType: String,
    val status: String,
    val createdAt: LocalDateTime,
    val categoryName: String
)
