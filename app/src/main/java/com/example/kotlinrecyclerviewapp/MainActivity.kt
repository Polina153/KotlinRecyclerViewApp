package com.example.kotlinrecyclerviewapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinrecyclerviewapp.databinding.ActivityMainBinding
import kotlin.io.path.Path

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RecyclerActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val data = arrayListOf(
            Pair(Data("Earth"), false),
            Pair(Data("Earth"), false),
            Pair(Data("Mars", ""), false),
            Pair(Data("Earth"), false),
            Pair(Data("Earth"), false),
            Pair(Data("Earth"), false),
            Pair(Data("Mars", null), false)
        )

        data.add(0, Pair(Data("Header"), false))

        adapter = RecyclerActivityAdapter(
            object : RecyclerActivityAdapter.OnListItemClickListener {
                override fun onItemClick(data: Data) {
                    Toast.makeText(this@MainActivity, data.someText, Toast.LENGTH_LONG).show()
                }
            },
            data
        )

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerActivityFAB.setOnClickListener {
            adapter.appendItem()
            binding.recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
        }

    }
}


class RecyclerActivityAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var data: MutableList<Pair<Data, Boolean>>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_EARTH -> EarthViewHolder(
                inflater.inflate(R.layout.activity_recycler_item_earth, parent, false) as View
            )

            TYPE_MARS ->
                MarsViewHolder(
                    inflater.inflate(R.layout.activity_recycler_item_mars, parent, false) as View
                )

            else -> HeaderViewHolder(
                inflater.inflate(R.layout.activity_recycler_item_header, parent, false) as View
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_EARTH -> {
                holder as EarthViewHolder
                holder.bind(data[position])
            }

            TYPE_MARS -> {
                holder as MarsViewHolder
                holder.bind(data[position])
            }

            else -> {
                holder as HeaderViewHolder
                holder.bind(data[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_HEADER
            data[position].first.someDescription.isNullOrBlank() -> TYPE_MARS
            else -> TYPE_EARTH
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun appendItem() {
        data.add(generateItem())
        //notifyDataSetChanged()
        notifyItemInserted(itemCount - 1)
    }

    private fun generateItem() = Pair(Data("Mars", ""), false)


    inner class EarthViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(dataItem: Pair<Data, Boolean>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.findViewById<TextView>(R.id.descriptionTextView).text =
                    dataItem.first.someDescription
                itemView.findViewById<ImageView>(R.id.wikiImageView)
                    .setOnClickListener { onListItemClickListener.onItemClick(dataItem.first) }
            }
        }
    }

    inner class MarsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(dataItem: Pair<Data, Boolean>) {
            itemView.findViewById<ImageView>(R.id.marsImageView)
                .setOnClickListener { onListItemClickListener.onItemClick(dataItem.first) }
            itemView.findViewById<ImageView>(R.id.moveItemDown).setOnClickListener { moveDown() }
            itemView.findViewById<ImageView>(R.id.moveItemUp).setOnClickListener { moveUp() }
            itemView.findViewById<TextView>(R.id.marsTextView)
                .setOnClickListener { toggleText() }
        }

        private fun toggleText() {
            data[layoutPosition] = data[layoutPosition].let {
                it.first to !it.second
        }
            //FIXME отладить появление/исчезновение текста
            if (data[layoutPosition].second) {
                itemView.findViewById<TextView>(R.id.marsDescriptionTextView).visibility =
                    View.VISIBLE
            } else{
                itemView.findViewById<TextView>(R.id.marsDescriptionTextView).visibility =
                    View.INVISIBLE
            }
            notifyItemChanged(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(dataItem: Pair<Data, Boolean>) {
            itemView.setOnClickListener { onListItemClickListener.onItemClick(dataItem.first) }
        }
    }

    interface OnListItemClickListener {
        fun onItemClick(data: Data)
    }

    companion object {
        private const val TYPE_EARTH = 0
        private const val TYPE_MARS = 1
        private const val TYPE_HEADER = 2
    }
}