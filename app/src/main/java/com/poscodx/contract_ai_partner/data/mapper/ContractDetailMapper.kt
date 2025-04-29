package com.poscodx.contract_ai_partner.data.mapper

import com.poscodx.contract_ai_partner.data.remote.dto.ContractDetailDto
import com.poscodx.contract_ai_partner.data.remote.dto.IncorrectTextDto
import com.poscodx.contract_ai_partner.data.remote.dto.PositionDto
import com.poscodx.contract_ai_partner.domain.model.ContractDetail
import com.poscodx.contract_ai_partner.domain.model.IncorrectText
import com.poscodx.contract_ai_partner.domain.model.Position

fun ContractDetailDto.toDomain(): ContractDetail = ContractDetail(
    id = id,
    name = name,
    type = type,
    url = url,
    status = status,
    categoryName = categoryName,
    totalPage = totalPage,
    incorrectTexts = incorrectTexts.map { it.toDomain() }
)

private fun IncorrectTextDto.toDomain() = IncorrectText(
    id = id,
    currentPage = currentPage,
    accuracy = accuracy,
    incorrectText = incorrectText,
    proofText = proofText,
    correctedText = correctedText,
    positions     = positions.map { it.toDomain() },        // ✅ 하이라이트 좌표
    positionParts = positionParts.map { it.toDomain() }
)

private fun PositionDto.toDomain() = Position(
    left   = left,
    top    = top,
    width  = width,
    height = height
)