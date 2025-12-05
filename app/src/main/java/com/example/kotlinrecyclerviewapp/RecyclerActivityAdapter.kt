package com.example.kotlinrecyclerviewapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerActivityAdapter(
    private val presenter: MainPresenterImpl,
    private val data: MutableList<Pair<Data, Boolean>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_EARTH -> EarthViewHolder(
                inflater.inflate(R.layout.activity_recycler_item_earth, parent, false)
            )
            TYPE_MARS -> MarsViewHolder(
                inflater.inflate(R.layout.activity_recycler_item_mars, parent, false)
            )
            else -> HeaderViewHolder(
                inflater.inflate(R.layout.activity_recycler_item_header, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            RecyclerActivityAdapter.TYPE_EARTH -> (holder as EarthViewHolder).bind(data[position])
            RecyclerActivityAdapter.TYPE_MARS -> (holder as MarsViewHolder).bind(data[position])
            else -> (holder as HeaderViewHolder).bind(data[position])
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> RecyclerActivityAdapter.TYPE_HEADER
            data[position].first.someDescription.isNullOrBlank() -> RecyclerActivityAdapter.TYPE_MARS
            else -> RecyclerActivityAdapter.TYPE_EARTH
        }
    }

    inner class EarthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataItem: Pair<Data, Boolean>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.findViewById<TextView>(R.id.descriptionTextView).text =
                    dataItem.first.someDescription
                itemView.findViewById<ImageView>(R.id.wikiImageView)
                    .setOnClickListener { presenter.onItemClick(dataItem.first) }
            }
        }
    }

    inner class MarsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ItemTouchHelperViewHolder {
        fun bind(dataItem: Pair<Data, Boolean>) {
            itemView.findViewById<ImageView>(R.id.marsImageView)
                .setOnClickListener { presenter.onItemClick(dataItem.first) }
            itemView.findViewById<ImageView>(R.id.moveItemDown).setOnClickListener { moveDown() }
            itemView.findViewById<ImageView>(R.id.moveItemUp).setOnClickListener { moveUp() }
            itemView.findViewById<TextView>(R.id.marsTextView).setOnClickListener { toggleText() }
        }

        private fun toggleText() {
            presenter.toggleItemText(layoutPosition)
           /* data[layoutPosition] = data[layoutPosition].let { it.first to !it.second }
            val textView = itemView.findViewById<TextView>(R.id.marsDescriptionTextView)
            textView.visibility = if (data[layoutPosition].second) View.VISIBLE else View.GONE
            notifyItemChanged(layoutPosition)*/
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { pos ->
                data.removeAt(pos).apply { data.add(pos - 1, this) }
                notifyItemMoved(pos, pos - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { pos ->
                data.removeAt(pos).apply { data.add(pos + 1, this) }
                notifyItemMoved(pos, pos + 1)
            }
        }

        override fun onItemSelected() { itemView.setBackgroundColor(Color.LTGRAY) }
        override fun onItemClear() { itemView.setBackgroundColor(0) }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataItem: Pair<Data, Boolean>) {
            itemView.setOnClickListener { presenter.onItemClick(dataItem.first) }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        presenter.onItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        presenter.onItemDismissed(position)
    }

    companion object {
        const val TYPE_EARTH = 0
        const val TYPE_MARS = 1
        const val TYPE_HEADER = 2
    }
}