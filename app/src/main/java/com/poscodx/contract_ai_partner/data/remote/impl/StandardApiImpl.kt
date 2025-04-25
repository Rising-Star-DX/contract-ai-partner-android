package com.poscodx.contract_ai_partner.data.remote.impl

import com.poscodx.contract_ai_partner.data.remote.api.StandardApi
import com.poscodx.contract_ai_partner.data.remote.dto.ApiResponse
import com.poscodx.contract_ai_partner.data.remote.dto.StandardDetailDto
import com.poscodx.contract_ai_partner.data.remote.dto.StandardDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class StandardApiImpl @Inject constructor(
    private val client: HttpClient
) : StandardApi {
    // 기준 문서 전체 조회
    override suspend fun getStandardList(): ApiResponse<List<StandardDto>> =
        client.get("/standards/android").body()

    // 기준 문서 단일 조회
    override suspend fun getStandardDetail(id: Long) =
        client.get("/standards/$id").body<ApiResponse<StandardDetailDto>>()
}