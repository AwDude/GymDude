package de.itdude.gymdude.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import de.itdude.gymdude.model.Workout
import de.itdude.gymdude.model.WorkoutPlan
import de.itdude.gymdude.repo.db.LiveRealmCollection
import de.itdude.gymdude.ui.fragment.WorkoutPlansFragmentDirections
import io.realm.RealmList
import javax.inject.Inject

class WorkoutPlansViewModel @Inject constructor() : AViewModel() {

    lateinit var workoutPlans: LiveRealmCollection<WorkoutPlan>
    val selectedPlan = MutableLiveData<WorkoutPlan>()
    val selectedWorkouts = LiveRealmCollection<Workout>(RealmList())
    val isEditable = ObservableBoolean(false)

    // declaring the function like this makes it usable as data binding callback
    val onSelectPlan = fun(position: Int) {
        selectedPlan.value = workoutPlans[position]
        selectedPlan.value?.let {
            selectedWorkouts.setValue(it.workouts)
        }
    }
    val onMoveWorkout = fun(from: Int, to: Int) {
        Log.e("ad", "moved workout")
    }

    override fun onCreate() {
        workoutPlans = repo.getWorkoutPlans()
    }

    // TODO create dialog
    fun addWorkout() = selectedPlan.value?.let { plan ->
        repo.addWorkout(plan, Workout("Workout ${plan.workouts.size}"))
        { error -> showToast(error) }
    }

    // TODO "you sure?" dialog
    fun removeWorkout(workout: Workout) {}

    fun startWorkout(workout: Workout) =
        navigate(WorkoutPlansFragmentDirections.actionShowWorkout(workout.toString()))

    // TODO create dialog
    fun addWorkoutPlan() {
        // TODO select new plan
        val newWorkoutPlan = WorkoutPlan("3er Split ${workoutPlans.size}")
        repo.addWorkoutPlan(newWorkoutPlan) { error -> showToast(error) }
    }

    // TODO "you sure?" dialog
    fun deleteWorkoutPlan() = selectedPlan.value?.let { plan ->
        repo.deleteWorkoutPlan(plan) { error -> showToast(error) }
    }

    // TODO
    fun renameWorkoutPlan() {

    }

    // TODO
    fun showWorkoutPlanHistory() {

    }

    fun toggleEditable() = isEditable.set(!isEditable.get())
}