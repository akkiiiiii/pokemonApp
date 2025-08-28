package com.pokemon


import android.app.Application
import com.pokemon.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PokemonApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PokemonApplication)
            modules(appModule)
        }
    }
}
