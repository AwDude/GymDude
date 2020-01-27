package de.itdude.gymdude.ui.fragment

import androidx.navigation.fragment.navArgs
import de.itdude.gymdude.BR
import de.itdude.gymdude.R
import de.itdude.gymdude.databinding.FragmentWorkoutBinding
import de.itdude.gymdude.viewmodel.WorkoutViewModel

class WorkoutFragment : AFragment<WorkoutViewModel, FragmentWorkoutBinding>() {

    val args: WorkoutFragmentArgs by navArgs()

    override fun getViewModelClass() = WorkoutViewModel::class
    override fun getLayoutID() = R.layout.fragment_workout
    override fun getViewModelBindingID() = BR.vm

}
