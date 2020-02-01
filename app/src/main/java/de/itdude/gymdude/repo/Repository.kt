package de.itdude.gymdude.repo

import android.content.res.Resources
import android.util.Log
import de.itdude.gymdude.R
import de.itdude.gymdude.repo.db.asLiveData
import de.itdude.gymdude.repo.db.model.BodyPart
import de.itdude.gymdude.repo.db.model.Exercise
import de.itdude.gymdude.util.LiveDataList
import io.realm.Realm
import io.realm.kotlin.where
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("RedundantLambdaArrow")
@Singleton
class Repository @Inject constructor(private val resources: Resources, private val db: Realm) {

    fun addExercise(exercise: Exercise, onError: (String) -> Unit) {
        if (db.where<Exercise>().equalTo("name", exercise.name).findFirst() == null) {
            db.executeTransactionAsync({ asyncDB ->
                val bodyPart =
                    asyncDB.where<BodyPart>().equalTo("name", exercise.bodyPart?.name).findFirst()
                if (bodyPart != null) {
                    exercise.bodyPart = bodyPart
                }
                asyncDB.copyToRealm(exercise)
            }, { _ -> onError(resources.getString(R.string.error_db)) })
        } else {
            onError(resources.getString(R.string.error_name_in_use))
        }
    }

    fun getExercises(): LiveDataList<Exercise> =
        db.where<Exercise>().sort("name").findAllAsync().asLiveData()

    fun deleteExercise(exercise: Exercise, onError: (String) -> Unit) {
        val name = exercise.name
        db.executeTransactionAsync({ asyncDB ->
            val dbExercise = asyncDB.where<Exercise>().equalTo("name", name).findAll()
            dbExercise.deleteAllFromRealm()
        }, { _ -> onError(resources.getString(R.string.error_db)) })
    }

}