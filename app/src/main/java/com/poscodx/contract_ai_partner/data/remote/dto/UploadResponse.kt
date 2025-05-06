package com.poscodx.contract_ai_partner.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UploadResultDto(val id: Long)

@Serializable
data class UploadResponse(
    val code: String,         // "S003"
    val message: String,      // "UPDATE SUCCESS"
    val data: UploadResultDto
)
