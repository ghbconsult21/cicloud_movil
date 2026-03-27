package com.example.cicloud.di

import com.example.cicloud.network.httpClient
import com.example.cicloud.repository.AuthRepository
import com.example.cicloud.viewmodels.LoginViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.singleOf

val appModule = module {
    single { httpClient }
    singleOf(::AuthRepository)
    viewModel { LoginViewModel(get()) }
}
