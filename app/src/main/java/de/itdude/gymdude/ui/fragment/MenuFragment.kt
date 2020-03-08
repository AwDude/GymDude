package de.itdude.gymdude.ui.fragment

import de.itdude.gymdude.BR
import de.itdude.gymdude.R
import de.itdude.gymdude.databinding.FragmentMenuBinding
import de.itdude.gymdude.viewmodel.MenuViewModel

class MenuFragment : AFragment<MenuViewModel, FragmentMenuBinding>() {

	override fun getViewModelClass() = MenuViewModel::class
	override fun getLayoutID() = R.layout.fragment_menu
	override fun getViewModelBindingID() = BR.vm

}
