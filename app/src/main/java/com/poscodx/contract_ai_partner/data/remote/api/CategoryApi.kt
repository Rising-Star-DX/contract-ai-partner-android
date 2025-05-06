package com.poscodx.contract_ai_partner.data.remote.api

import com.poscodx.contract_ai_partner.data.remote.dto.CategoryDto

interface CategoryApi {
    suspend fun getCategories(): List<CategoryDto>
}