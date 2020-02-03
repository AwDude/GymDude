package de.itdude.gymdude.repo.db

import android.content.res.Resources
import de.itdude.gymdude.R
import de.itdude.gymdude.model.BodyPart
import de.itdude.gymdude.model.Exercise
import io.realm.Realm
import io.realm.kotlin.where
import javax.inject.Inject
import javax.inject.Singleton


@Suppress("RedundantLambdaArrow")
@Singleton
class DataBase @Inject constructor(private val realm: Realm) {

    fun addExercise(exercise: Exercise, onNameError: () -> Unit, onDbError: () -> Unit) {
        val name = exercise.name
        val bodyPartName = exercise.bodyPart?.name

        realm.executeTransactionAsync({ asyncDB ->
            if (asyncDB.where<Exercise>().equalTo("name", name).findFirst() == null) {
                asyncDB.where<BodyPart>().equalTo("name", bodyPartName).findFirst()
                    ?.let { existingBodyPart -> exercise.bodyPart = existingBodyPart }
                asyncDB.copyToRealm(exercise)
            } else {
                onNameError()
            }
        }, { _ -> onDbError() })
    }

    fun getExercises() = realm.where<Exercise>().findAllAsync().asLiveData()

    fun deleteExercise(exercise: Exercise, onDbError: () -> Unit) {
        val name = exercise.name

        realm.executeTransactionAsync({ asyncDB ->
            asyncDB.where<Exercise>().equalTo("name", name).findAll().deleteAllFromRealm()
        }, { _ -> onDbError() })
    }
}