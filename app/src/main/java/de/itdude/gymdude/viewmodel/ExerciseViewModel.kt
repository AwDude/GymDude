package de.itdude.gymdude.viewmodel

import androidx.appcompat.widget.SearchView
import de.itdude.gymdude.R
import de.itdude.gymdude.model.BodyPart
import de.itdude.gymdude.model.Exercise
import de.itdude.gymdude.repo.db.LiveRealmResults
import de.itdude.gymdude.util.daysAgo
import io.realm.Sort
import javax.inject.Inject

class ExerciseViewModel @Inject constructor() : AViewModel(), SearchView.OnQueryTextListener {

    private var query = ""
    lateinit var exercises: LiveRealmResults<Exercise>

    // declaring the function like this makes it usable as data binding callback
    val onSort = fun(position: Int) {
        when (position) {
            0 -> exercises.sort(Exercise::lastTimeDone, Sort.ASCENDING)
            1 -> exercises.sort(Exercise::lastTimeDone, Sort.DESCENDING)
            2 -> exercises.sort(Exercise::name, Sort.ASCENDING)
            3 -> exercises.sort(Exercise::name, Sort.DESCENDING)
        }
    }

    override fun onCreate() {
        val results = repo.getExercises()
        results.sort(Exercise::name)
        results.filter = { it.name.contains(query) }
        exercises = results
    }

    fun addExercise() {
        val newExercise = Exercise("Bankdrücken ${exercises.size}", BodyPart("Brust"))
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
        exercises.filterAll()
        return true
    }

}