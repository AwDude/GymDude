package de.itdude.gymdude.viewmodel

import android.os.Bundle
import javax.inject.Inject

class WorkoutViewModel @Inject constructor() : AViewModel() {

    lateinit var workoutName: String
    val exercises = ArrayList<Bundle>()

    // declaring the function like this makes it usable as data binding callback
    val onGetTabLabel = fun(position: Int) = exercises[position].getString(EXERCISE_KEY)

    init {
        exercises.add(Bundle().apply { putString(EXERCISE_KEY, "Exercise 0") })
        exercises.add(Bundle().apply { putString(EXERCISE_KEY, "Exercise 1") })
        exercises.add(Bundle().apply { putString(EXERCISE_KEY, "Exercise 2") })
        exercises.add(Bundle().apply { putString(EXERCISE_KEY, "Exercise 3") })
        exercises.add(Bundle().apply { putString(EXERCISE_KEY, "Exercise 4") })
    }

    companion object {
        const val EXERCISE_KEY = "exerciseKey"
    }

}