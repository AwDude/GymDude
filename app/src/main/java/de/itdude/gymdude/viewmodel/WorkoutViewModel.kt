package de.itdude.gymdude.viewmodel

import android.os.Bundle
import de.itdude.gymdude.model.Exercise
import javax.inject.Inject

class WorkoutViewModel @Inject constructor() : AViewModel() {

	companion object {
		const val EXERCISE_KEY = "exerciseKey"
	}

	lateinit var workoutName: String
	val exercises = ArrayList<Exercise>()

	// Data binding callbacks
	val getTabLabel = fun(position: Int) = exercises[position].toString()
	val getBundle = fun(exercise: Exercise) = Bundle().apply { putString(EXERCISE_KEY, exercise.name) }

	init {
		exercises.add(Exercise("Exercise 0"))
		exercises.add(Exercise("Exercise 1"))
		exercises.add(Exercise("Exercise 2"))
		exercises.add(Exercise("Exercise 3"))
	}

}