package com.poscodx.contract_ai_partner.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val code: String,
    val message: String,
    val data: T
)
