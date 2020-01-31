package de.itdude.gymdude.viewmodel

import de.itdude.gymdude.BR
import de.itdude.gymdude.repo.db.model.BodyPart
import de.itdude.gymdude.repo.db.model.Exercise
import de.itdude.gymdude.util.LiveDataList
import javax.inject.Inject

class ExerciseViewModel @Inject constructor() : AViewModel() {

    // provides RecyclerView the fields where to put created bindings (via BindingAdapter)
    val viewModelBinding: Int = BR.vm
    val itemBinding: Int = BR.item

    lateinit var items: LiveDataList<Exercise>

    override fun onCreate() {
        items = repo.getExercises()
    }

    fun addItem() {
        val newExercise = Exercise("Bankdrücken ${items.size}", BodyPart("Brust"))
        repo.addExercise(newExercise) { error -> showToast(error) }
    }

}