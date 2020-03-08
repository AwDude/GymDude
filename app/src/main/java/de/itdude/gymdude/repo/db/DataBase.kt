package de.itdude.gymdude.repo.db

import de.itdude.gymdude.model.BodyPart
import de.itdude.gymdude.model.Exercise
import de.itdude.gymdude.model.Workout
import de.itdude.gymdude.model.WorkoutPlan
import de.itdude.gymdude.util.asFilterableLive
import de.itdude.gymdude.util.asLive
import de.itdude.gymdude.util.equalTo
import io.realm.Realm
import io.realm.kotlin.where
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("RedundantLambdaArrow")
@Singleton
class DataBase @Inject constructor(realm: Realm) {

	private var realm = realm
		get() {
			if (field.isClosed) field = Realm.getDefaultInstance()
			return field
		}

	fun addExercise(exercise: Exercise, onNameError: () -> Unit, onDbError: () -> Unit) {
		val name = exercise.name
		val bodyPartName = exercise.bodyPart?.name

		realm.executeTransactionAsync({ aRealm ->
			if (aRealm.where<Exercise>().equalTo(Exercise::name, name).findFirst() == null) {
				aRealm.where<BodyPart>().equalTo(BodyPart::name, bodyPartName).findFirst()?.let { existingBodyPart ->
					exercise.bodyPart = existingBodyPart
				}
				aRealm.copyToRealm(exercise)
			} else {
				onNameError()
			}
		}, { _ -> onDbError() })
	}

	fun getExercises() = realm.where<Exercise>().findAllAsync().asFilterableLive()

	fun deleteExercise(exercise: Exercise, onDbError: () -> Unit) = if (exercise.isValid) {
		realm.executeTransaction { exercise.deleteFromRealm() }
	} else {
		onDbError()
	}

	fun getWorkoutPlans() = realm.where<WorkoutPlan>().findAllAsync().asLive()

	fun addWorkoutPlan(workoutPlan: WorkoutPlan, onSuccess: () -> Unit, onNameError: () -> Unit) =
		if (realm.where<WorkoutPlan>().equalTo(WorkoutPlan::name, workoutPlan.name).findFirst() == null) {
			realm.executeTransaction { realm.copyToRealm(workoutPlan) }
			onSuccess()
		} else {
			onNameError()
		}

	fun deleteWorkoutPlan(workoutPlan: WorkoutPlan, onSuccess: () -> Unit, onDbError: () -> Unit) = if (workoutPlan.isValid) {
		realm.executeTransaction { workoutPlan.deleteFromRealm() }
		onSuccess()
	} else {
		onDbError()
	}

	fun addWorkout(workoutPlan: WorkoutPlan, workout: Workout, onNameError: () -> Unit, onDbError: () -> Unit) = when {
		!workoutPlan.isValid -> onDbError()
		workoutPlan.workouts.any { it.name == workout.name } -> onNameError()
		else -> realm.executeTransaction {
			workout.index = workoutPlan.workouts.size.toLong()
			val mWorkout = realm.copyToRealm(workout)
			workoutPlan.workouts.add(mWorkout)
		}
	}

	fun moveWorkout(workoutPlan: WorkoutPlan, from: Int, to: Int, onDbError: () -> Unit) = if (workoutPlan.isValid) {
		realm.executeTransaction { workoutPlan.workouts.move(from, to) }
	} else {
		onDbError()
	}

	fun deleteWorkout(workoutPlan: WorkoutPlan, workout: Workout, onDbError: () -> Unit) = if (workoutPlan.isValid && workout.isValid) {
		realm.executeTransaction {
			workoutPlan.workouts.remove(workout)
			workout.deleteFromRealm()
		}
	} else {
		onDbError()
	}
}