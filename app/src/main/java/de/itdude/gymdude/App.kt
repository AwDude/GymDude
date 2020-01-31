package de.itdude.gymdude

import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import de.itdude.gymdude.di.AppInjector
import io.realm.Realm
import javax.inject.Inject


class App : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        Realm.init(this)
    }

    override fun androidInjector() = dispatchingAndroidInjector
}