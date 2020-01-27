package de.itdude.gymdude.ui.adapter.recyclerview

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import de.itdude.gymdude.util.LiveDataList


class RecyclerViewAdapter(
    recyclerView: RecyclerView,
    private val items: LiveDataList<*>,
    private val itemLayout: Int,
    private val viewModel: ViewModel?,
    private val viewModelBinding: Int?,
    private val itemBinding: Int?
) : RecyclerView.Adapter<RecyclerViewAdapter.BindingHolder>(), LiveDataList.ListObserver,
    ItemMoveCallback.ItemTouchHelperContract {

    var dragColor: Int? = null
    private var dragViewID: Int? = null
    private var onDragRequested: ((RecyclerView.ViewHolder) -> Unit)? = null
    private val antiViewGlitchObserver = object : LiveDataList.ListObserver {
        override fun notifyDataSetChanged() {}
        override fun notifyItemChanged(position: Int) {}
        override fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {}
        override fun notifyItemInserted(position: Int) {}
        override fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {}
        override fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {}
        override fun notifyItemMoved(fromPosition: Int, toPosition: Int) {}
        override fun notifyItemRemoved(position: Int) {
            recyclerView.getChildAt(position)?.run {
                recyclerView.removeViewAt(position)
            }
        }
    }

    fun useDragHandle(dragViewID: Int, touchHelper: ItemTouchHelper) {
        this.dragViewID = dragViewID
        onDragRequested = { viewHolder -> touchHelper.startDrag(viewHolder) }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        items.observe(recyclerView.context, antiViewGlitchObserver)
        items.observe(recyclerView.context, this)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        items.stopObserve(this)
        items.stopObserve(antiViewGlitchObserver)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            itemLayout,
            parent,
            false
        )
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        if (viewModel != null && viewModelBinding != null) {
            holder.binding.setVariable(viewModelBinding, viewModel)
        }
        if (itemBinding != null) {
            holder.binding.setVariable(itemBinding, items[position])
        }
        dragViewID?.let {
            holder.itemView.findViewById<View>(it).setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    onDragRequested?.invoke(holder)
                }
                false
            }
        }
    }

    override fun getItemCount() = items.size

    override fun onItemMoved(fromPosition: Int, toPosition: Int): Boolean {
        items.move(fromPosition, toPosition)
        return true
    }

    override fun onItemSelected(viewHolder: RecyclerView.ViewHolder) {
        dragColor?.let {
            viewHolder.itemView.background.setTint(it)
        }
    }

    override fun onItemCleared(viewHolder: RecyclerView.ViewHolder) {
        viewHolder.itemView.background.setTintList(null)
    }

    class BindingHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}
