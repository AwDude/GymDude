package de.itdude.gymdude.repo

import android.content.res.Resources
import de.itdude.gymdude.R
import de.itdude.gymdude.model.Exercise
import de.itdude.gymdude.model.Workout
import de.itdude.gymdude.model.WorkoutPlan
import de.itdude.gymdude.repo.db.DataBase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val resources: Resources, private val db: DataBase) {

    fun addExercise(exercise: Exercise, onError: (String) -> Unit) = db.addExercise(exercise, {
        onError(resources.getString(R.string.error_name_in_use))
    }, {
        onError(resources.getString(R.string.error_db))
    })

    fun getExercises() = db.getExercises()

    fun deleteExercise(exercise: Exercise, onError: (String) -> Unit) =
        db.deleteExercise(exercise) { onError(resources.getString(R.string.error_db)) }

    fun addWorkoutPlan(workoutPlan: WorkoutPlan, onSuccess: () -> Unit, onError: (String) -> Unit) =
        db.addWorkoutPlan(workoutPlan, onSuccess, {
            onError(resources.getString(R.string.error_name_in_use))
        }, {
            onError(resources.getString(R.string.error_db))
        })

    fun getWorkoutPlans() = db.getWorkoutPlans()

    fun deleteWorkoutPlan(
        workoutPlan: WorkoutPlan, onSuccess: () -> Unit, onError: (String) -> Unit
    ) = db.deleteWorkoutPlan(workoutPlan, onSuccess, { onError(resources.getString(R.string.error_db)) })

    fun addWorkout(workoutPlan: WorkoutPlan, workout: Workout, onError: (String) -> Unit) =
        db.addWorkout(workoutPlan, workout, {
            onError(resources.getString(R.string.error_name_in_use))
        }, {
            onError(resources.getString(R.string.error_db))
        })

    fun moveWorkout(workoutPlan: WorkoutPlan, from: Int, to: Int, onError: (String) -> Unit) =
        db.moveWorkout(workoutPlan, from, to) { onError(resources.getString(R.string.error_db)) }

    fun deleteWorkout(workoutPlan: WorkoutPlan, workout: Workout, onError: (String) -> Unit) =
        db.deleteWorkout(workoutPlan, workout) { onError(resources.getString(R.string.error_db)) }
}