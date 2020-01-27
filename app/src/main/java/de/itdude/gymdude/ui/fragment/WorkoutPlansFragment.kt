package de.itdude.gymdude.ui.fragment

import de.itdude.gymdude.BR
import de.itdude.gymdude.R
import de.itdude.gymdude.databinding.FragmentWorkoutPlansBinding
import de.itdude.gymdude.viewmodel.WorkoutPlansViewModel

class WorkoutPlansFragment : AFragment<WorkoutPlansViewModel, FragmentWorkoutPlansBinding>() {

    override fun getViewModelClass() = WorkoutPlansViewModel::class
    override fun getLayoutID() = R.layout.fragment_workout_plans
    override fun getViewModelBindingID() = BR.vm

}
