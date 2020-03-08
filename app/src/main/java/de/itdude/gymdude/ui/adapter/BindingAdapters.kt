@file:Suppress("unused")

package de.itdude.gymdude.ui.adapter

import android.content.ContextWrapper
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.itdude.gymdude.BR
import de.itdude.gymdude.ui.MainActivity
import de.itdude.gymdude.ui.adapter.recyclerview.ItemMoveCallback
import de.itdude.gymdude.ui.adapter.recyclerview.RecyclerViewAdapter
import de.itdude.gymdude.util.LiveList
import de.itdude.gymdude.util.MutableLiveList
import kotlin.math.abs
import kotlin.math.max

// --- HELPER FUNCTIONS / OBJECT ---

private fun getBindingID(fieldName: String?): Int? = fieldName?.let { field ->
	BR::class.java.getDeclaredField(field).getInt(null)
}

// --- RECYCLER VIEW ---

@BindingAdapter("items",
	"itemLayout",
	"itemBinding",
	"viewModelBinding",
	"viewModel",
	"dragByLongPress",
	"dragColor",
	"dragViewID",
	"getItemID",
	requireAll = false)
fun <T> RecyclerView.bindItems(items: MutableLiveList<T>,
							   itemLayout: Int,
							   itemBinding: String?,
							   viewModelBinding: String?,
							   viewModel: ViewModel?,
							   dragByLongPress: Boolean?,
							   dragColor: Int?,
							   dragViewID: Int?,
							   getItemID: ((T) -> Long)?) =
	bindItems(items, itemLayout, itemBinding, viewModelBinding, viewModel, dragByLongPress, dragColor, dragViewID, items::move, getItemID)

@BindingAdapter("items",
	"itemLayout",
	"itemBinding",
	"viewModelBinding",
	"viewModel",
	"dragByLongPress",
	"dragColor",
	"dragViewID",
	"onItemMove",
	"getItemID",
	requireAll = false)
fun <T> RecyclerView.bindItems(items: LiveList<T>,
							   itemLayout: Int,
							   itemBinding: String?,
							   viewModelBinding: String?,
							   viewModel: ViewModel?,
							   dragByLongPress: Boolean?,
							   dragColor: Int?,
							   dragViewID: Int?,
							   onItemMove: ((Int, Int) -> Unit)?,
							   getItemID: ((T) -> Long)?) = adapter ?: run {
	val rvAdapter =
		RecyclerViewAdapter(this, items, itemLayout, viewModel, getBindingID(viewModelBinding), getBindingID(itemBinding), getItemID)
	if (dragByLongPress == true || dragViewID != null) {
		if (onItemMove == null) {
			throw NullPointerException("onItemMove must not be null if dragByLongPress or dragViewID is specified!")
		}
		val contract = object : ItemMoveCallback.ItemTouchHelperContract {
			override fun onItemMoved(fromPosition: Int, toPosition: Int): Boolean {
				onItemMove(fromPosition, toPosition)
				return true
			}

			override fun onItemSelected(viewHolder: RecyclerView.ViewHolder) {
				dragColor?.let { color -> viewHolder.itemView.background.setTint(color) }
			}

			override fun onItemCleared(viewHolder: RecyclerView.ViewHolder) {
				viewHolder.itemView.background.setTintList(null)
			}
		}
		val callback = ItemMoveCallback(contract, dragByLongPress ?: false)
		val touchHelper = ItemTouchHelper(callback)
		dragViewID?.run { rvAdapter.useDragHandle(dragViewID, touchHelper) }
		touchHelper.attachToRecyclerView(this)
	}
	adapter = rvAdapter
}

// --- SPINNER ---

@BindingAdapter("onSelect")
fun Spinner.bindOnSelect(onSelect: (Int?) -> Unit) {
	onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
		override fun onNothingSelected(parent: AdapterView<*>?) = onSelect(null)
		override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = onSelect(position)
	}
}

@BindingAdapter("items", "selectedLayout", "dropDownLayout", "hideSelected", requireAll = false)
fun Spinner.bindItems(items: LiveList<*>, selectedLayout: Int?, dropDownLayout: Int?, hideSelected: Boolean?) = adapter ?: run {
	adapter = SpinnerAdapter(this, items, selectedLayout, hideSelected == true).apply {
		dropDownLayout?.run { setDropDownViewResource(dropDownLayout) }
	}
}

// --- VIEW PAGER ---

@BindingAdapter("pageDataList", "fragmentClass", "getBundle", requireAll = false)
fun <T> ViewPager2.bindPages(pageDataList: List<T>, fragmentClass: String, getBundle: ((T) -> Bundle?)?) = adapter ?: run {
	@Suppress("UNCHECKED_CAST") val clazz = Class.forName(fragmentClass) as Class<Fragment>

	adapter = object : FragmentStateAdapter(findFragment<Fragment>()) {
		override fun getItemCount() = pageDataList.size
		override fun createFragment(position: Int) = clazz.newInstance().apply {
			getBundle?.run { arguments = getBundle(pageDataList[position]) }
		}
	}
}

@BindingAdapter("zoom", "zoomFade")
fun ViewPager2.bindZoom(zoom: Float, zoomFade: Float) = this.setPageTransformer { page, position ->
	if (-1 <= position && position <= 1) {
		val scaleFactor = max(zoom, 1 - abs(position))
		val verticalMargin = height * (1 - scaleFactor) / 2
		val horizontalMargin = width * (1 - scaleFactor) / 2

		page.translationX = if (position < 0) {
			horizontalMargin - verticalMargin / 2
		} else {
			horizontalMargin + verticalMargin / 2
		}
		page.scaleX = scaleFactor
		page.scaleY = scaleFactor
		page.alpha = zoomFade + (((scaleFactor - zoomFade) / (1 - zoomFade)) * (1 - zoomFade))
	} else {
		page.alpha = 0f
	}
}

@BindingAdapter("viewPager", "getTabLabel")
fun TabLayout.bindMediator(viewPager2: ViewPager2, getTabLabel: (Int) -> String) = TabLayoutMediator(this, viewPager2) { tab, position ->
	tab.text = getTabLabel(position)
}.attach()

// --- SEARCH VIEW ---

@BindingAdapter("onQuery")
fun SearchView.bindQueryListener(onQuery: SearchView.OnQueryTextListener) = setOnQueryTextListener(onQuery)

@BindingAdapter("hideOnExpand", "collapsedWidth", requireAll = false)
fun SearchView.bindHideOnExpand(view: View?, collapsedWidth: Float?) {
	val expanded = layoutParams.width
	val collapsed = collapsedWidth?.let { (it / context.resources.displayMetrics.density).toInt() }

	if (isIconified && collapsed != null) {
		layoutParams.width = collapsed
	}
	setOnSearchClickListener {
		view?.visibility = View.GONE
		collapsed?.run { layoutParams.width = expanded }
	}
	setOnCloseListener {
		view?.visibility = View.VISIBLE
		collapsed?.run { layoutParams.width = collapsed }
		false
	}
}

@BindingAdapter("autoCollapse")
fun SearchView.bindAutoCollapse(autoCollapseEnabled: Boolean) {
	if (!autoCollapseEnabled) return

	var activity = context
	while (activity !is MainActivity) {
		activity = (activity as ContextWrapper).baseContext
	}

	activity.clearFocusViews.add(this)
	setOnQueryTextFocusChangeListener { _, hasFocus ->
		if (!hasFocus && query.isNullOrBlank()) {
			isIconified = true
		}
	}
}

// --- VIEW ---

@BindingAdapter("autoFocusClear")
fun View.bindAutoFocusClear(autoFocusClearEnabled: Boolean) {
	val activity = context
	if (autoFocusClearEnabled && activity is MainActivity) {
		activity.clearFocusViews.add(this)
	}
}