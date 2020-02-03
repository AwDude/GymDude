package de.itdude.gymdude.repo

import android.content.res.Resources
import de.itdude.gymdude.R
import de.itdude.gymdude.model.Exercise
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

}