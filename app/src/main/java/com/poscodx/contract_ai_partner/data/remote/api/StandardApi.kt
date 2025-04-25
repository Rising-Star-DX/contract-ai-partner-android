package com.poscodx.contract_ai_partner.data.remote.api

import com.poscodx.contract_ai_partner.data.remote.dto.ApiResponse
import com.poscodx.contract_ai_partner.data.remote.dto.StandardDetailDto
import com.poscodx.contract_ai_partner.data.remote.dto.StandardDto

interface StandardApi {
    suspend fun getStandardList(): ApiResponse<List<StandardDto>>
    suspend fun getStandardDetail(id: Long): ApiResponse<StandardDetailDto>
}