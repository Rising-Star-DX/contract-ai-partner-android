package com.poscodx.contract_ai_partner.di

import com.poscodx.contract_ai_partner.data.repository.ContractRepositoryImpl
import com.poscodx.contract_ai_partner.data.repository.StandardRepositoryImpl
import com.poscodx.contract_ai_partner.domain.repository.ContractRepository
import com.poscodx.contract_ai_partner.domain.repository.StandardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideContractRepository(
        impl: ContractRepositoryImpl
    ): ContractRepository = impl

    @Provides
    @Singleton
    fun provideStandardRepository(
        impl: StandardRepositoryImpl
    ): StandardRepository = impl
}