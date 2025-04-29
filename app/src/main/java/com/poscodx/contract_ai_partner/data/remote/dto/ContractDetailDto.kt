package com.poscodx.contract_ai_partner.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContractDetailDto(
    val id: Long,
    val name: String,
    val type: String,
    val url: String,
    val status: String,
    val categoryName: String,
    val totalPage: Int,
    @SerialName("incorrectTexts")
    val incorrectTexts: List<IncorrectTextDto>
)

@Serializable
data class IncorrectTextDto(
    val id: Long,
    val currentPage: Int,
    val accuracy: Double,
    val incorrectText: String,
    val proofText: String,
    val correctedText: String,
    val positions: List<PositionDto> = emptyList(),
    val positionParts: List<PositionDto> = emptyList()
)

@Serializable
data class PositionDto(
    val left: Double,
    val top: Double,
    val width: Double,
    val height: Double
)

data class PageLayoutInfo(
    val index: Int,
    val offsetX: Float,
    val offsetY: Float,
    val width:  Float,
    val height: Float
)