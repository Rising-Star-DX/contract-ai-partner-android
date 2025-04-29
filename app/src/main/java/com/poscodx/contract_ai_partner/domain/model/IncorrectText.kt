package com.poscodx.contract_ai_partner.domain.model

data class Position(val left: Double, val top: Double, val width: Double, val height: Double)

data class IncorrectText(
    val id: Long,
    val currentPage: Int,
    val accuracy: Double,
    val incorrectText: String,
    val proofText: String,
    val correctedText: String,
    val positions: List<Position>,           // ✅ 추가
    val positionParts: List<Position>
)