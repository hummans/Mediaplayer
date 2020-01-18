package com.mediaplayer.app.di.modules

import androidx.lifecycle.ViewModel
import com.mediaplayer.app.ViewModelFactory
import com.mediaplayer.db.SongsRepository
import com.mediaplayer.ui.home.HomeViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider
import kotlin.reflect.KClass

@Module
class ViewModelModule {
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @Retention(AnnotationRetention.RUNTIME)
    @MapKey
    internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

    @Provides
    fun viewModelFactory(map: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelFactory {
        return ViewModelFactory(map)
    }


    @Provides
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun homeViewModel(songsRepository: SongsRepository?): ViewModel {
        return HomeViewModel(songsRepository!!)
    }
}