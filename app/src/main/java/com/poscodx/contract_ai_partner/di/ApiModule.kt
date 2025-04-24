package com.poscodx.contract_ai_partner.di

import com.poscodx.contract_ai_partner.data.remote.api.ContractApi
import com.poscodx.contract_ai_partner.data.remote.impl.ContractApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    @Binds
    @Singleton
    abstract fun bindContractApi(
        impl: ContractApiImpl
    ): ContractApi
}