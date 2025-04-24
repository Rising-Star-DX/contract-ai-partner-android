package com.poscodx.contract_ai_partner.domain.repository

import com.poscodx.contract_ai_partner.domain.model.Contract

interface ContractRepository {
    suspend fun fetchContracts(): List<Contract>
}