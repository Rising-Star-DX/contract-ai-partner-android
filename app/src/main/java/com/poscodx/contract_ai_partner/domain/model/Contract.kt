package com.poscodx.contract_ai_partner.domain.model

import java.time.LocalDateTime

data class Contract(
    val id: Long,
    val name: String,
    val fileType: String,
    val aiStatus: AiStatus,
    val createdAt: LocalDateTime,
    val category: String
)

enum class AiStatus { SUCCESS, AI_FAILED, ANALYZING }
