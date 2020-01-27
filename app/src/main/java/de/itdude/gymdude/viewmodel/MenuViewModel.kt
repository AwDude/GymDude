package de.itdude.gymdude.viewmodel

import de.itdude.gymdude.ui.fragment.MenuFragmentDirections
import javax.inject.Inject

class MenuViewModel @Inject constructor() : AViewModel() {

    fun showSettings() {
        navigate(MenuFragmentDirections.actionShowSettings())
    }

    fun showExercises() {
        navigate(MenuFragmentDirections.actionShowExercises())
    }

    fun showWorkout() {
        navigate(MenuFragmentDirections.actionShowWorkoutPlans())
    }
}