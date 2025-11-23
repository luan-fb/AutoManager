package com.luanferreira.automanager.di

import com.luanferreira.automanager.data.repository.AutenticacaoRepositoryImpl
import com.luanferreira.automanager.data.repository.VeiculoRepositoryImpl
import com.luanferreira.automanager.domain.repository.AutenticacaoRepository
import com.luanferreira.automanager.domain.repository.VeiculoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAutenticacaoRepository(
        impl: AutenticacaoRepositoryImpl
    ): AutenticacaoRepository

    @Binds
    @Singleton
    abstract fun bindVeiculoRepository(
        impl: VeiculoRepositoryImpl
    ): VeiculoRepository
}