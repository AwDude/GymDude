package de.itdude.gymdude.repo

import android.content.res.Resources
import de.itdude.gymdude.R
import de.itdude.gymdude.repo.db.asLiveData
import de.itdude.gymdude.repo.db.model.BodyPart
import de.itdude.gymdude.repo.db.model.Exercise
import io.realm.Realm
import io.realm.kotlin.where
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("RedundantLambdaArrow")
@Singleton
class Repository @Inject constructor(private val resources: Resources, private val db: Realm) {

    fun addExercise(exercise: Exercise, onError: (String) -> Unit) {
        val name = exercise.name
        val bodyPartName = exercise.bodyPart?.name

        db.executeTransactionAsync({ asyncDB ->
            if (asyncDB.where<Exercise>().equalTo("name", name).findFirst() == null) {
                asyncDB.where<BodyPart>().equalTo("name", bodyPartName).findFirst()
                    ?.let { existingBodyPart -> exercise.bodyPart = existingBodyPart }
                asyncDB.copyToRealm(exercise)
            } else {
                onError(resources.getString(R.string.error_name_in_use))
            }
        }, { _ -> onError(resources.getString(R.string.error_db)) })
    }

    fun getExercises() = db.where<Exercise>().findAllAsync().asLiveData()

    fun deleteExercise(exercise: Exercise, onError: (String) -> Unit) {
        val name = exercise.name

        db.executeTransactionAsync({ asyncDB ->
            asyncDB.where<Exercise>().equalTo("name", name).findAll().deleteAllFromRealm()
        }, { _ -> onError(resources.getString(R.string.error_db)) })
    }

}