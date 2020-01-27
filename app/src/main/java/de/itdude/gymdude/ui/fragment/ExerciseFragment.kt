package de.itdude.gymdude.ui.fragment

import de.itdude.gymdude.BR
import de.itdude.gymdude.R
import de.itdude.gymdude.databinding.FragmentExerciseBinding
import de.itdude.gymdude.viewmodel.ExerciseViewModel

class ExerciseFragment : AFragment<ExerciseViewModel, FragmentExerciseBinding>() {

    override fun getViewModelClass() = ExerciseViewModel::class
    override fun getLayoutID() = R.layout.fragment_exercise
    override fun getViewModelBindingID() = BR.vm

}
