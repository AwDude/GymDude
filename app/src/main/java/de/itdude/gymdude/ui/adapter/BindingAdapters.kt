package de.itdude.gymdude.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import de.itdude.gymdude.ui.adapter.recyclerview.ItemMoveCallback
import de.itdude.gymdude.ui.adapter.recyclerview.RecyclerViewAdapter
import de.itdude.gymdude.util.LiveDataList


@BindingAdapter(
    "items", "itemLayout", "itemBinding", "viewModelBinding", "viewModel", "dragByLongPress",
    "dragColor", "dragViewID", requireAll = false
)
fun RecyclerView.bindItems(
    items: LiveDataList<*>, itemLayout: Int, itemBinding: Int?, viewModelBinding: Int?,
    viewModel: ViewModel?, dragByLongPress: Boolean?, dragColor: Int?, dragViewID: Int?
) = adapter ?: let {
    val rvAdapter = RecyclerViewAdapter(
        this, items, itemLayout, viewModel, viewModelBinding, itemBinding
    )
    if (dragByLongPress == true || dragViewID != null) {
        rvAdapter.dragColor = dragColor
        val callback = ItemMoveCallback(rvAdapter, dragByLongPress ?: false)
        val touchHelper = ItemTouchHelper(callback)
        dragViewID?.let { rvAdapter.useDragHandle(dragViewID, touchHelper) }
        touchHelper.attachToRecyclerView(this)
    }
    adapter = rvAdapter
}

@BindingAdapter(
    "onSelect", "items", "selectedLayout", "dropDownLayout", "hideSelected", requireAll = false
)
fun Spinner.bindOnSelect(
    onSelect: (Int) -> Unit, items: List<*>, selectedLayout: Int, dropDownLayout: Int?,
    hideSelected: Boolean?
) = adapter ?: let {
    adapter = if (hideSelected == true) {
        createHiddenSelectionArrayAdapter(selectedLayout, items, this)
    } else {
        ArrayAdapter(context, selectedLayout, items)
    }.apply {
        dropDownLayout?.let { setDropDownViewResource(dropDownLayout) }
    }

    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onSelect(position)
        }
    }
}

private fun createHiddenSelectionArrayAdapter(
    selectedLayout: Int, items: List<*>, spinner: Spinner
) = object : ArrayAdapter<Any>(spinner.context, selectedLayout, items) {

    private val hideParams = ViewGroup.LayoutParams(-1, 1)
    private val showParams = ViewGroup.LayoutParams(-1, -2)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
        super.getDropDownView(position, convertView, parent).apply {
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