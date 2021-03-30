package de.itdude.gymdude

import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import de.itdude.gymdude.di.AppInjector
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject

class App : Application(), HasAndroidInjector {
	@Inject
	lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

	override fun onCreate() {
		super.onCreate()
		AppInjector.init(this)
		initRealm()
	}

	private fun initRealm() {
		Realm.init(this)
		// TODO later a migration may be needed
		val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
		Realm.setDefaultConfiguration(config)
	}

	override fun androidInjector() = dispatchingAndroidInjector
}