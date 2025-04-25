package com.poscodx.contract_ai_partner.data.mapper

import com.poscodx.contract_ai_partner.data.remote.dto.StandardDetailDto
import com.poscodx.contract_ai_partner.domain.model.StandardDetail

fun StandardDetailDto.toDomain() = StandardDetail(
    id, name, type, url, status, createdAt, categoryName
)