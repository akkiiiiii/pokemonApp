package com.pokemon.di


import com.pokemon.model.PokemonApi
import com.pokemon.model.PokemonRepository
import com.pokemon.viewModel.PokemonDetailViewModel
import com.pokemon.viewModel.PokemonListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    // HTTP Logging Interceptor
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // OkHttp Client
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit instance
    single {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API Service
    single { get<Retrofit>().create(PokemonApi::class.java) }

    // Repository
    single { PokemonRepository(get()) }

    // ViewModels for Compose
    viewModel { PokemonListViewModel(get()) }
    viewModel { PokemonDetailViewModel(get()) }
}
