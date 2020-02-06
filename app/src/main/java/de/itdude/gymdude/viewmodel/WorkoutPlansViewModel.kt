package de.itdude.gymdude.viewmodel

import de.itdude.gymdude.ui.fragment.WorkoutPlansFragmentDirections
import de.itdude.gymdude.util.LiveDataList
import javax.inject.Inject

class WorkoutPlansViewModel @Inject constructor() : AViewModel() {

    val workoutPlans = LiveDataList<String>()
    val workouts = LiveDataList<String>()

    // declaring the function like this makes it usable as data binding callback
    val onSelectPlan = fun(position: Int) {
    }

    init {
        workoutPlans.addAll(arrayListOf("3er Split", "4er Split", "5er Split"))
    }

    // TODO create / modify dialog
    fun addWorkout() = workouts.add("Workout ${workouts.size}")

    // TODO "you sure?" dialog
    fun removeWorkout(item: String) = workouts.remove(item)

    fun startWorkout(name: String) =
        navigate(WorkoutPlansFragmentDirections.actionShowWorkout(name))

}