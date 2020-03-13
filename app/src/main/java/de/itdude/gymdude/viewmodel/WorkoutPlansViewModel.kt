package de.itdude.gymdude.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import de.itdude.gymdude.model.Workout
import de.itdude.gymdude.model.WorkoutPlan
import de.itdude.gymdude.repo.db.LiveRealmCollection
import de.itdude.gymdude.ui.fragment.WorkoutPlansFragmentDirections
import javax.inject.Inject

class WorkoutPlansViewModel @Inject constructor() : AViewModel() {

	lateinit var workoutPlans: LiveRealmCollection<WorkoutPlan>
	val selectedPlanIndex = MutableLiveData<Int>()
	val selectedWorkouts = LiveRealmCollection<Workout>()
	val isEditable = ObservableBoolean()

	private val selectedPlan: WorkoutPlan?
		get() = selectedPlanIndex.value?.let { index -> if (index >= 0) workoutPlans[index] else null }

	private val selectedPlanIndexObserver = Observer<Int> {
		selectedPlan?.let { plan -> selectedWorkouts.setValue(plan.workouts) } ?: selectedWorkouts.clear()
	}

	// Data binding callback
	val onMoveWorkout = fun(from: Int, to: Int) = selectedPlan?.let { plan ->
		repo.moveWorkout(plan, from, to) { error -> showToast(error) }
	}

	// Data binding callback
	val getItemID = fun(workout: Workout) = workout.index

	override fun onCreate() {
		workoutPlans = repo.getWorkoutPlans()
		selectedPlanIndex.observeForever(selectedPlanIndexObserver)
	}

	override fun onCleared() = super.onCleared().also {
		selectedPlanIndex.removeObserver(selectedPlanIndexObserver)
	}

	// TODO create dialog
	fun addWorkout() = selectedPlan?.let { plan ->
		repo.addWorkout(plan, Workout("Workout ${selectedWorkouts.size}")) { error -> showToast(error) }
	}

	// TODO "you sure?" dialog
	fun removeWorkout(workout: Workout) = selectedPlan?.let { plan ->
		repo.deleteWorkout(plan, workout) { error -> showToast(error) }
	}

	fun startWorkout(workout: Workout) = navigate(WorkoutPlansFragmentDirections.actionShowWorkout(workout.name))

	// TODO create dialog
	fun addWorkoutPlan() {
		// TODO select new plan
		val newWorkoutPlan = WorkoutPlan("3er Split ${workoutPlans.size} dfgdfgdfgasd")
		repo.addWorkoutPlan(newWorkoutPlan, {
			selectedPlanIndex.value = workoutPlans.size - 1
			isEditable.set(true)
		}, { error -> showToast(error) })
	}

	// TODO "you sure?" dialog
	fun deleteWorkoutPlan() = selectedPlan?.let { plan ->
		repo.deleteWorkoutPlan(plan, {
			selectedPlanIndex.value = if (workoutPlans.isEmpty()) -1 else 0
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
