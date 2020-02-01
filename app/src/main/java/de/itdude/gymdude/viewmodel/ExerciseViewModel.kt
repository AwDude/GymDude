package de.itdude.gymdude.viewmodel

import de.itdude.gymdude.BR
import de.itdude.gymdude.R
import de.itdude.gymdude.repo.db.model.BodyPart
import de.itdude.gymdude.repo.db.model.Exercise
import de.itdude.gymdude.util.LiveDataList
import de.itdude.gymdude.util.daysAgo
import javax.inject.Inject

class ExerciseViewModel @Inject constructor() : AViewModel() {

    // provides RecyclerView the fields where to put created bindings (via BindingAdapter)
    val viewModelBinding: Int = BR.vm
    val itemBinding: Int = BR.exercise

    lateinit var exercises: LiveDataList<Exercise>

    override fun onCreate() {
        exercises = repo.getExercises()
    }

    fun addExercise() {
        val newExercise = Exercise("Bankdrücken ${exercises.size}", BodyPart("Brust"))
        repo.addExercise(newExercise) { error -> showToast(error) }
    }

    fun deleteExercise(exercise: Exercise) =
        repo.deleteExercise(exercise) { error -> showToast(error) }

    fun lastTimeDoneText(exercise: Exercise): String =
        when (val days = exercise.getLastTimeDone()?.daysAgo()) {
            null -> resources.getString(R.string.never_text)
            0 -> resources.getString(R.string.today_text)
            1 -> resources.getString(R.string.yesterday_text)
            else -> resources.getString(R.string.days_ago_text, days.toString())
        }
}