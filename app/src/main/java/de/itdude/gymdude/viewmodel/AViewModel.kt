package de.itdude.gymdude.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections

abstract class AViewModel : ViewModel() {
    lateinit var navigate: ((NavDirections) -> Unit)
}