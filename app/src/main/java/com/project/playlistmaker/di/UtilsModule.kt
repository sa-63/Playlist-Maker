package com.project.playlistmaker.di

import com.markodevcic.peko.PermissionRequester
import com.project.playlistmaker.utils.ResourceProvider
import com.project.playlistmaker.utils.ResourceProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class UtilsModule {
    val utilsModule = module {
        //ResourceProvider
        single<ResourceProvider> {
            ResourceProviderImpl(androidContext())
        }

        //PermissionRequester
        single {
            PermissionRequester.instance()
        }
    }
}