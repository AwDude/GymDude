package de.itdude.gymdude.util

import androidx.lifecycle.Observer
import io.realm.RealmObject

class FilterRealmResultsObserver<T : RealmObject>(
    private val resultList: LiveDataList<T>,
    private val filter: (T) -> Boolean
) : Observer<MutableList<T>?> {

    override fun onChanged(list: MutableList<T>?) {
        var filteredItemCount = 0

        list?.forEach { exercise ->
            if (filter(exercise)) {
                val index = resultList.indexOfFirst { it.isValid && it == exercise }

                if (index < 0) {
                    resultList.add(filteredItemCount, exercise)
                } else {
                    resultList.removeRange(filteredItemCount, index)
                }

                filteredItemCount++
            }
        }

        if (filteredItemCount < resultList.size) {
            resultList.removeRange(filteredItemCount, resultList.size)
        }
    }

}

/*{ list ->
            var filteredItemCount = 0

            list?.forEachIndexed { i, exercise ->
                if (exercise.name.contains(query)) {
                    var index = -1
                    for (j in i until exercises.size) {
                        val existing = exercises[j]
                        if (existing.isValid && existing == exercise) {
                            index = j
                        }
                    }

                    //val index = exercises.indexOfFirst { it.isValid && it == exercise }
                    if (index < 0) {
                        exercises.add(filteredItemCount, exercise)
                    } else {
                        exercises.removeRange(filteredItemCount, index)
                    }

                    filteredItemCount++
                }
            }

            if (filteredItemCount < exercises.size) {
                exercises.removeRange(filteredItemCount, exercises.size)
            }

            *//* Slower alternative

            val indices = ArrayList<Int>()

            list.forEach { exercise ->
                if (exercise.name.contains(query)) {
                    val i = exercises.indexOfFirst { it.isValid && it == exercise }
                    if (i < 0) {
                        indices.add(exercises.size)
                        exercises.add(exercise)
                    } else {
                        indices.add(i)
                    }
                }
            }

            for (i in exercises.size - 1 downTo 0) {
                if (!indices.contains(i)) {
                    exercises.removeAt(i)
                }
            }*//*
        }*/