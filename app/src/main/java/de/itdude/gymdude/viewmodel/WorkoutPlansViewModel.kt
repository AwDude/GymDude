package de.itdude.gymdude.viewmodel

import androidx.databinding.ObservableBoolean
import de.itdude.gymdude.model.Workout
import de.itdude.gymdude.model.WorkoutPlan
import de.itdude.gymdude.repo.db.LiveRealmCollection
import de.itdude.gymdude.ui.fragment.WorkoutPlansFragmentDirections
import javax.inject.Inject

class WorkoutPlansViewModel @Inject constructor() : AViewModel() {

    lateinit var workoutPlans: LiveRealmCollection<WorkoutPlan>
    private var selectedPlan: WorkoutPlan? = null
    val selectedWorkouts = LiveRealmCollection<Workout>()
    val isEditable = ObservableBoolean()

    // declaring the function like this makes it usable as data binding callback
    val onSelectPlan = fun(position: Int?) = if (position == null) {
        selectedPlan = null
        selectedWorkouts.clear()
    } else {
        val plan = workoutPlans[position]
        selectedPlan = plan
        selectedWorkouts.setValue(plan.workouts)
    }

    val onMoveWorkout = fun(from: Int, to: Int) = selectedPlan?.let { plan ->
        repo.moveWorkout(plan, from, to) { error -> showToast(error) }
    }

    val getItemID = fun(workout: Workout) = workout.index

    override fun onCreate() {
        workoutPlans = repo.getWorkoutPlans()
    }

    // TODO create dialog
    fun addWorkout() = selectedPlan?.let { plan ->
        repo.addWorkout(plan, Workout("Workout ${selectedWorkouts.size}"))
        { error -> showToast(error) }
    }

    // TODO "you sure?" dialog
    fun removeWorkout(workout: Workout) = selectedPlan?.let { plan ->
        repo.deleteWorkout(plan, workout) { error -> showToast(error) }
    }

    fun startWorkout(workout: Workout) =
        navigate(WorkoutPlansFragmentDirections.actionShowWorkout(workout.toString()))

    // TODO create dialog
    fun addWorkoutPlan() {
        // TODO select new plan
        val newWorkoutPlan = WorkoutPlan("3er Split ${workoutPlans.size}")
        repo.addWorkoutPlan(newWorkoutPlan, { isEditable.set(true) }, { error -> showToast(error) })
    }

    // TODO "you sure?" dialog
    fun deleteWorkoutPlan() = selectedPlan?.let { plan ->
        repo.deleteWorkoutPlan(plan, {
            selectedWorkouts.clear()
            if (workoutPlans.isEmpty()) isEditable.set(false)
        }, { error -> showToast(error) })
    }

    // TODO
    fun renameWorkoutPlan() {

    }

    // TODO
    fun showWorkoutPlanHistory() {

    }

    fun toggleEditable() = isEditable.set(if (workoutPlans.isEmpty()) false else !isEditable.get())
}