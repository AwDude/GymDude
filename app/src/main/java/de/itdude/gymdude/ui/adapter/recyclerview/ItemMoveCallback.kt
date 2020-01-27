package de.itdude.gymdude.ui.adapter.recyclerview

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class ItemMoveCallback(private val contract: ItemTouchHelperContract, private val enableLongPress: Boolean) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled() = enableLongPress

    override fun isItemViewSwipeEnabled() = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {}

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = contract.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder != null) {
            contract.onItemSelected(viewHolder)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        contract.onItemCleared(viewHolder)
    }

    interface ItemTouchHelperContract {
        fun onItemMoved(fromPosition: Int, toPosition: Int): Boolean
        fun onItemSelected(viewHolder: RecyclerView.ViewHolder)
        fun onItemCleared(viewHolder: RecyclerView.ViewHolder)
    }

}