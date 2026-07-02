package com.example.cicloud.di

import com.example.cicloud.network.httpClient
import com.example.cicloud.repository.AsistenciaRepository
import com.example.cicloud.repository.AuthRepository
import com.example.cicloud.viewmodels.AsistenciaViewModel
import com.example.cicloud.viewmodels.LoginViewModel
import com.example.cicloud.viewmodels.HorarioViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.singleOf

val appModule = module {
    single { httpClient }
    singleOf(::AuthRepository)
    singleOf(::AsistenciaRepository)
    viewModel { LoginViewModel(get()) }
    viewModel { AsistenciaViewModel(get()) }
    viewModel { HorarioViewModel(get()) }
}
