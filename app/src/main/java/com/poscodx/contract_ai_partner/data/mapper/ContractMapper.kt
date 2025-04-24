package com.poscodx.contract_ai_partner.data.mapper

import com.poscodx.contract_ai_partner.data.remote.dto.ContractDto
import com.poscodx.contract_ai_partner.domain.model.AiStatus
import com.poscodx.contract_ai_partner.domain.model.Contract
import java.time.LocalDateTime

fun ContractDto.toDomain(): Contract =
    Contract(
        id        = id,
        name      = name,
        fileType  = type,
        aiStatus  = AiStatus.valueOf(status.replace('-', '_')),
        createdAt = LocalDateTime.parse(createdAt),
        category  = categoryName
    )