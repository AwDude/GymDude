package de.itdude.gymdude.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import de.itdude.gymdude.repo.Repository
import javax.inject.Inject

abstract class AViewModel : ViewModel() {

    @Inject
    protected lateinit var repo: Repository

    lateinit var navigate: ((NavDirections) -> Unit)

    lateinit var showToast: ((String) -> Unit)

    @Inject @VisibleForTesting
    protected fun onRepoInjected() = onCreate()

    protected open fun onCreate() {}

}