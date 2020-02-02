package de.itdude.gymdude.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import de.itdude.gymdude.BR
import de.itdude.gymdude.R
import de.itdude.gymdude.databinding.FragmentExerciseBinding
import de.itdude.gymdude.viewmodel.ExerciseViewModel
import io.realm.Sort


class ExerciseFragment : AFragment<ExerciseViewModel, FragmentExerciseBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = super.onCreateView(inflater, container, savedInstanceState).also {
        val compatActivity = activity
        if (compatActivity is AppCompatActivity) {
            compatActivity.setSupportActionBar(binding.exerciseToolbar)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exercise_toolbar, menu)

        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            queryHint = getString(R.string.exercise_search_hint)
            setOnQueryTextListener(viewModel)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.menuSortByNameAsc -> {
            viewModel.sort("name", Sort.ASCENDING)
            true
        }
        R.id.menuSortByNameDesc -> {
            viewModel.sort("name", Sort.DESCENDING)
            true
        }
        R.id.menuSortByDateAsc -> {
            viewModel.sort("lastTimeDone", Sort.ASCENDING)
            true
        }
        R.id.menuSortByDateDesc -> {
            viewModel.sort("lastTimeDone", Sort.DESCENDING)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun getViewModelClass() = ExerciseViewModel::class
    override fun getLayoutID() = R.layout.fragment_exercise
    override fun getViewModelBindingID() = BR.vm

}
