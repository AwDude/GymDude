package de.itdude.gymdude.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import de.itdude.gymdude.BR
import de.itdude.gymdude.R
import de.itdude.gymdude.databinding.FragmentWorkoutExerciseBinding
import de.itdude.gymdude.viewmodel.WorkoutExerciseViewModel
import de.itdude.gymdude.viewmodel.WorkoutViewModel

class WorkoutExerciseFragment :
    AFragment<WorkoutExerciseViewModel, FragmentWorkoutExerciseBinding>() {

    override fun getViewModelClass() = WorkoutExerciseViewModel::class
    override fun getLayoutID() = R.layout.fragment_workout_exercise
    override fun getViewModelBindingID() = BR.vm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = super.onCreateView(inflater, container, savedInstanceState).also {
        viewModel.exerciseName = arguments?.getString(WorkoutViewModel.EXERCISE_KEY)
    }

}
