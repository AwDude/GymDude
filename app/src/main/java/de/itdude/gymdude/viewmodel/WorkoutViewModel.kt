package de.itdude.gymdude.viewmodel

import android.util.Log
import de.itdude.gymdude.BR
import de.itdude.gymdude.util.LiveDataList
import javax.inject.Inject

class WorkoutViewModel @Inject constructor() : AViewModel() {

    // provides RecyclerView the fields where to put created bindings (via BindingAdapter)
    val viewModelBinding: Int = BR.vm
    val itemBinding: Int = BR.item

    val workoutPlans = LiveDataList<String>()
    val workouts = LiveDataList<String>()

    var selectedPlan: String? = null
    var selectedPos: Int? = null

    // declaring the function like this makes it usable as data binding callback
    val onSelectPlan = fun(position: Int) {
        /*val plan = selectedPlan
        val pos = selectedPos
        selectedPos = position
        selectedPlan = workoutPlans.removeAt(position)
        if (plan != null && pos != null) {
            workoutPlans.add(pos, plan)
        }*/
        Log.e("asd", position.toString())
    }

    init {
        workoutPlans.addAll(arrayListOf("3er Split", "4er Split", "5er Split"))
    }

    fun addItem() = workouts.add("Item ${workouts.size}")

    fun removeItem(item: String) = workouts.remove(item)

}