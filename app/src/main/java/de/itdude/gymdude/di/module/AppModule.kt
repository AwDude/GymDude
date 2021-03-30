package de.itdude.gymdude.di.module

import android.app.Application
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class AppModule {

	@Provides
	@Singleton
	fun provideRealm(): Realm = Realm.getDefaultInstance()

	@Provides
	@Singleton
	fun provideResources(application: Application): Resources = application.resources

}