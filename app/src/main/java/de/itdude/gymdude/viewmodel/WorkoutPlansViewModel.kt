package de.itdude.gymdude.viewmodel

import androidx.databinding.ObservableBoolean
import de.itdude.gymdude.model.Workout
import de.itdude.gymdude.model.WorkoutPlan
import de.itdude.gymdude.repo.db.LiveRealmCollection
import de.itdude.gymdude.ui.fragment.WorkoutPlansFragmentDirections
import io.realm.RealmList
import javax.inject.Inject

class WorkoutPlansViewModel @Inject constructor() : AViewModel() {

    lateinit var workoutPlans: LiveRealmCollection<WorkoutPlan>
    private lateinit var selectedPlan: WorkoutPlan
    val selectedWorkouts = LiveRealmCollection<Workout>(RealmList())
    val isEditable = ObservableBoolean(false)

    // declaring the function like this makes it usable as data binding callback
    val onSelectPlan = fun(position: Int) {
        selectedPlan = workoutPlans[position]
        selectedWorkouts.setValue(selectedPlan.workouts)
    }
    val onMoveWorkout = fun(from: Int, to: Int) {
        repo.moveWorkout(selectedPlan, from, to) { error -> showToast(error) }
    }

    override fun onCreate() {
        workoutPlans = repo.getWorkoutPlans()
    }

    // TODO create dialog
    fun addWorkout() = repo.addWorkout(selectedPlan, Workout("Workout ${selectedWorkouts.size}"))
    { error -> showToast(error) }

    // TODO "you sure?" dialog
    fun removeWorkout(workout: Workout) = repo.deleteWorkout(selectedPlan, workout) { error -> showToast(error) }

    fun startWorkout(workout: Workout) =
        navigate(WorkoutPlansFragmentDirections.actionShowWorkout(workout.toString()))

    // TODO create dialog
    fun addWorkoutPlan() {
        // TODO select new plan
        val newWorkoutPlan = WorkoutPlan("3er Split ${workoutPlans.size}")
        repo.addWorkoutPlan(newWorkoutPlan) { error -> showToast(error) }
    }

    // TODO "you sure?" dialog
    fun deleteWorkoutPlan() = repo.deleteWorkoutPlan(selectedPlan) { error -> showToast(error) }

    // TODO
    fun renameWorkoutPlan() {

    }

    // TODO
    fun showWorkoutPlanHistory() {

    }

    fun toggleEditable() = isEditable.set(!isEditable.get())
}