package de.itdude.gymdude.viewmodel

import androidx.databinding.ObservableBoolean
import de.itdude.gymdude.ui.fragment.WorkoutPlansFragmentDirections
import de.itdude.gymdude.util.LiveList
import de.itdude.gymdude.util.MutableLiveList
import javax.inject.Inject

class WorkoutPlansViewModel @Inject constructor() : AViewModel() {

    val workoutPlans = LiveList(listOf("3er Split", "4er Split", "5er Split"))
    val workouts = MutableLiveList<String>()
    val isEditable = ObservableBoolean(false)

    // declaring the function like this makes it usable as data binding callback
    val onSelectPlan = fun(position: Int) {
    }

    // TODO create / modify dialog
    fun addWorkout() = workouts.add("Workout ${workouts.size}")

    // TODO "you sure?" dialog
    fun removeWorkout(item: String) = workouts.remove(item)

    fun startWorkout(name: String) =
        navigate(WorkoutPlansFragmentDirections.actionShowWorkout(name))

    fun addWorkoutPlan() {

    }

    fun deleteWorkoutPlan() {

    }

    fun renameWorkoutPlan() {

    }

    fun toggleEditable() = isEditable.set(!isEditable.get())

}