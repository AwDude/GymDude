package de.itdude.gymdude.ui.fragment

import de.itdude.gymdude.BR
import de.itdude.gymdude.R
import de.itdude.gymdude.databinding.FragmentSettingsBinding
import de.itdude.gymdude.viewmodel.SettingsViewModel

class SettingsFragment : AFragment<SettingsViewModel, FragmentSettingsBinding>() {

	override fun getViewModelClass() = SettingsViewModel::class
	override fun getLayoutID() = R.layout.fragment_settings
	override fun getViewModelBindingID() = BR.vm

}
