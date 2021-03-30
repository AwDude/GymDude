package de.itdude.gymdude.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.doOnDetach
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import de.itdude.gymdude.util.getLifecycleOwner

class SpinnerAdapter<T>(private val spinner: Spinner, items: List<T>, selectedLayout: Int?, private val hideSelected: Boolean = false) :
	ArrayAdapter<T>(spinner.context, selectedLayout ?: android.R.layout.simple_spinner_dropdown_item, items) {

	private val hideParams = ViewGroup.LayoutParams(-1, 1)
	private val showParams = ViewGroup.LayoutParams(-1, -2)
	private val observer = Observer<Any?> { notifyDataSetChanged() }

	init {
		if (items is LiveData<*>) {
			items.observe(spinner.context.getLifecycleOwner(), observer)
			spinner.doOnDetach { items.removeObserver(observer) }
		}
	}

	override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View =
		super.getDropDownView(position, convertView, parent).apply {
			if (hideSelected) {
				if (position == spinner.selectedItemPosition) {
					convertView?.layoutParams = hideParams
					convertView?.visibility = View.GONE
					layoutParams = hideParams
					visibility = View.GONE
				} else if (convertView != null && convertView.visibility == View.GONE) {
					convertView.visibility = View.VISIBLE
					convertView.layoutParams = showParams
				}
			}
		}

}