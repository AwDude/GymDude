package de.itdude.gymdude.viewmodel

import de.itdude.gymdude.BR
import de.itdude.gymdude.util.LiveDataList
import javax.inject.Inject

class ExerciseViewModel @Inject constructor() : AViewModel() {

    // provides RecyclerView the fields where to put created bindings (via BindingAdapter)
    val viewModelBinding: Int = BR.vm
    val itemBinding: Int = BR.item

    val items = LiveDataList<String>()

    fun addItem() {
        items.add("Item ${items.size}")
        repo.test()
    }
}