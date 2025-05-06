package com.poscodx.contract_ai_partner.domain.usecase

import com.poscodx.contract_ai_partner.domain.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCategoriesUseCase @Inject constructor(
    private val categoryRepo: CategoryRepository
) { suspend operator fun invoke() = categoryRepo.fetchCategories() }