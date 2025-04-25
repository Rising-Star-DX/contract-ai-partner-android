package com.poscodx.contract_ai_partner.data.mapper

import com.poscodx.contract_ai_partner.data.remote.dto.StandardDto
import com.poscodx.contract_ai_partner.domain.model.Standard
import java.time.LocalDateTime

fun StandardDto.toDomain(): Standard = Standard(
    id           = id,
    fileName     = name,
    fileType     = type,
    status       = status,
    createdAt    = LocalDateTime.parse(createdAt),
    categoryName = categoryName
)