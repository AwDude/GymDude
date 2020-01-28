package de.itdude.gymdude.ui.adapter

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import de.itdude.gymdude.ui.adapter.recyclerview.ItemMoveCallback
import de.itdude.gymdude.ui.adapter.recyclerview.RecyclerViewAdapter
import de.itdude.gymdude.util.LiveDataList
import kotlin.math.abs
import kotlin.math.max


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

@BindingAdapter("pageBundles", "fragmentClass")
fun ViewPager2.bindPages(pages: List<Bundle>, fragmentClass: String) = adapter ?: let {
    @Suppress("UNCHECKED_CAST") val clazz = Class.forName(fragmentClass) as Class<Fragment>
    adapter = object : FragmentStateAdapter(findFragment<Fragment>()) {
        override fun getItemCount() = pages.size
        override fun createFragment(position: Int) = clazz.newInstance().apply {
            arguments = pages[position]
        }
    }
}

@BindingAdapter("zoom", "zoomFade")
fun ViewPager2.bindZoom(zoom: Float, zoomFade: Float) = this.setPageTransformer { page, position ->
    if (-1 <= position && position <= 1) {
        val scaleFactor = max(zoom, 1 - abs(position))
        val vertMargin = height * (1 - scaleFactor) / 2
        val horzMargin = width * (1 - scaleFactor) / 2

        page.translationX = if (position < 0) {
            horzMargin - vertMargin / 2
        } else {
            horzMargin + vertMargin / 2
        }
        page.scaleX = scaleFactor
        page.scaleY = scaleFactor
        page.alpha = zoomFade + (((scaleFactor - zoomFade) / (1 - zoomFade)) * (1 - zoomFade))
    } else {
        page.alpha = 0f
    }
}