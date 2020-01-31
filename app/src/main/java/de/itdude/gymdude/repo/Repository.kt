package de.itdude.gymdude.repo

import android.content.res.Resources
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
class Repository @Inject constructor(private val resources: Resources) {

    fun addExercise(exercise: Exercise, onError: (String) -> Unit): Unit = withDB { db ->
        if (db.where<Exercise>().equalTo("name", exercise.name).findFirst() == null) {
            db.executeTransactionAsync({ asyncDB ->
                val bodyPart = asyncDB.where<BodyPart>().equalTo("name", exercise.bodyPart?.name).findFirst()
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
        Realm.getDefaultInstance().where<Exercise>().findAllAsync().asLiveData()

    private inline fun <R> withDB(runWithDB: (Realm) -> R) =
        Realm.getDefaultInstance().use { runWithDB(it) }

}