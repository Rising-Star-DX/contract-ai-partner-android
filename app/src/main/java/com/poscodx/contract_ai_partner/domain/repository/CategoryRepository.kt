package com.poscodx.contract_ai_partner.domain.repository

import com.poscodx.contract_ai_partner.domain.model.Category

interface CategoryRepository {
    suspend fun fetchCategories(): List<Category>
}