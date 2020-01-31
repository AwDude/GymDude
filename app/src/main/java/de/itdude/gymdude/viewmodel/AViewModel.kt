package de.itdude.gymdude.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import de.itdude.gymdude.repo.Repository
import javax.inject.Inject

abstract class AViewModel : ViewModel() {

    @Inject
    protected lateinit var repo: Repository

    lateinit var navigate: ((NavDirections) -> Unit)
}