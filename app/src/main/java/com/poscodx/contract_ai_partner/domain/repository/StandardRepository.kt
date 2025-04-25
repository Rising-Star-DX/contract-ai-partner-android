package com.poscodx.contract_ai_partner.domain.repository

import com.poscodx.contract_ai_partner.domain.model.Standard
import com.poscodx.contract_ai_partner.domain.model.StandardDetail

interface StandardRepository {
    suspend fun fetchStandards(): List<Standard>
    suspend fun fetchDetail(id: Long): StandardDetail
}