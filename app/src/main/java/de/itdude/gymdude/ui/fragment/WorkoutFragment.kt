package de.itdude.gymdude.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import de.itdude.gymdude.BR
import de.itdude.gymdude.R
import de.itdude.gymdude.databinding.FragmentWorkoutBinding
import de.itdude.gymdude.viewmodel.WorkoutViewModel

class WorkoutFragment : AFragment<WorkoutViewModel, FragmentWorkoutBinding>() {

	private val args: WorkoutFragmentArgs by navArgs()

	override fun getViewModelClass() = WorkoutViewModel::class
	override fun getLayoutID() = R.layout.fragment_workout
	override fun getViewModelBindingID() = BR.vm

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.workoutName = args.workoutName
	}

}
