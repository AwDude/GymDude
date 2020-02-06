package de.itdude.gymdude.ui.fragment

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import de.itdude.gymdude.di.Injectable
import de.itdude.gymdude.util.autoCleared
import de.itdude.gymdude.viewmodel.AViewModel
import javax.inject.Inject
import kotlin.reflect.KClass


@Suppress("MemberVisibilityCanBePrivate")
abstract class AFragment<VM : AViewModel, BIND : ViewDataBinding> : Fragment(), Injectable {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory
    protected lateinit var viewModel: VM
    protected var binding by autoCleared<BIND>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get<VM>(getViewModelClass().java)
        viewModel.navigate = findNavController()::navigate
        viewModel.showToast = ::showToast

        binding = DataBindingUtil.inflate(inflater, getLayoutID(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.setVariable(getViewModelBindingID(), viewModel)

        return binding.root
    }

    abstract fun getViewModelClass(): KClass<VM>

    abstract fun getViewModelBindingID(): Int

    abstract fun getLayoutID(): Int

    protected fun showToast(text: String) {
        if (Looper.myLooper() == null) {
            activity?.runOnUiThread {
                Toast.makeText(activity?.applicationContext, text, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(activity?.applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }

}
