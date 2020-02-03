package de.itdude.gymdude.viewmodel

import androidx.appcompat.widget.SearchView
import de.itdude.gymdude.BR
import de.itdude.gymdude.R
import de.itdude.gymdude.repo.db.LiveRealmResults
import de.itdude.gymdude.model.BodyPart
import de.itdude.gymdude.model.Exercise
import de.itdude.gymdude.repo.db.FilterRealmResultsObserver
import de.itdude.gymdude.util.LiveDataList
import de.itdude.gymdude.util.daysAgo
import io.realm.Sort
import javax.inject.Inject

class ExerciseViewModel @Inject constructor() : AViewModel(), SearchView.OnQueryTextListener {

    // provides RecyclerView the fields where to put created bindings (via BindingAdapter)
    val viewModelBinding: Int = BR.vm
    val itemBinding: Int = BR.exercise

    val exercises = LiveDataList<Exercise>()
    private val filterObserver =
        FilterRealmResultsObserver(exercises) {
            it.name.contains(query)
        }
    private var query = ""
    private lateinit var results: LiveRealmResults<Exercise>

    override fun onCreate() {
        results = repo.getExercises()
        results.sort("name")
        results.observeForever(filterObserver)
    }

    fun addExercise() {
        val newExercise = Exercise("Bankdrücken ${results.size}", BodyPart("Brust"))
        repo.addExercise(newExercise) { error -> showToast(error) }
    }

    fun deleteExercise(exercise: Exercise) {
        if (exercise.isValid) {
            repo.deleteExercise(exercise) { error -> showToast(error) }
        }
    }

    fun lastTimeDoneText(exercise: Exercise): String =
        when (val days = exercise.getLastTimeDone()?.daysAgo()) {
            null -> resources.getString(R.string.never_text)
            0 -> resources.getString(R.string.today_text)
            1 -> resources.getString(R.string.yesterday_text)
            else -> resources.getString(R.string.days_ago_text, days.toString())
        }

    override fun onQueryTextSubmit(query: String?) = true

    override fun onQueryTextChange(newText: String?): Boolean {
        query = newText ?: ""
        results.notifyObserver()
        return true
    }

    override fun onCleared() = super.onCleared().also {
        results.removeObserver(filterObserver)
    }

    fun sort(fieldName: String, order: Sort) = results.sort(fieldName, order)

}