package de.itdude.gymdude.di.module

import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideRealm(): Realm = Realm.getDefaultInstance()

}