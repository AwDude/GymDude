package de.itdude.gymdude.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import de.itdude.gymdude.di.Injectable
import de.itdude.gymdude.util.autoCleared
import de.itdude.gymdude.viewmodel.AViewModel
import javax.inject.Inject
import kotlin.reflect.KClass


abstract class AFragment<VM : AViewModel, BIND : ViewDataBinding> : Fragment(), Injectable {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory
    @Suppress("MemberVisibilityCanBePrivate")
    protected lateinit var viewModel: VM
    @Suppress("MemberVisibilityCanBePrivate")
    protected var binding by autoCleared<BIND>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutID(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get<VM>(getViewModelClass().java)
        viewModel.navigate = { direction -> findNavController().navigate(direction) }
        binding.lifecycleOwner = viewLifecycleOwner

        binding.setVariable(getViewModelBindingID(), viewModel)
    }

    abstract fun getViewModelClass(): KClass<VM>

    abstract fun getViewModelBindingID(): Int

    abstract fun getLayoutID(): Int

}
