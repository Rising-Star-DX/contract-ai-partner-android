package com.poscodx.contract_ai_partner.data.repository

import com.poscodx.contract_ai_partner.data.mapper.toDomain
import com.poscodx.contract_ai_partner.data.remote.api.CategoryApi
import com.poscodx.contract_ai_partner.domain.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val api: CategoryApi
) : CategoryRepository {
    override suspend fun fetchCategories() =
        api.getCategories().map { it.toDomain() }
}