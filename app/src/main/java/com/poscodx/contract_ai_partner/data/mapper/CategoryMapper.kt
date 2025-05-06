package com.poscodx.contract_ai_partner.data.mapper

import com.poscodx.contract_ai_partner.data.remote.dto.CategoryDto
import com.poscodx.contract_ai_partner.domain.model.Category

fun CategoryDto.toDomain() = Category(id, name)