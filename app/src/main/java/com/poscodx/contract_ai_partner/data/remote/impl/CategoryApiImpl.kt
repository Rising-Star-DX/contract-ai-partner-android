package com.poscodx.contract_ai_partner.data.remote.impl

import com.poscodx.contract_ai_partner.data.remote.api.CategoryApi
import com.poscodx.contract_ai_partner.data.remote.dto.ApiResponse
import com.poscodx.contract_ai_partner.data.remote.dto.CategoryDto
import com.poscodx.contract_ai_partner.data.remote.dto.requireData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryApiImpl @Inject constructor(
    private val client: HttpClient
) : CategoryApi {
    override suspend fun getCategories(): List<CategoryDto> =
        client.get("/categories")
            .body<ApiResponse<List<CategoryDto>>>().requireData()
}