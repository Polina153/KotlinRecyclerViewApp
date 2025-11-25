package com.example.kotlinrecyclerviewapp

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
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinrecyclerviewapp.databinding.ActivityMainBinding

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
            Data("Earth"),
            Data("Earth"),
            Data("Mars", ""),
            Data("Earth"),
            Data("Earth"),
            Data("Earth"),
            Data("Mars", null)
        )
        binding.recyclerView.adapter = RecyclerActivityAdapter(
            object : RecyclerActivityAdapter.OnListItemClickListener {
                override fun onItemClick(data: Data) {
                    Toast.makeText(this@MainActivity, data.someText, Toast.LENGTH_SHORT).show()
                }
            },
            data
        )
    }
}


class RecyclerActivityAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private var data: List<Data>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_EARTH) {
            EarthViewHolder(
                inflater.inflate(R.layout.activity_recycler_item_earth, parent, false) as View
            )
        } else {
            MarsViewHolder(
                inflater.inflate(R.layout.activity_recycler_item_mars, parent, false) as View
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_EARTH) {
            holder as EarthViewHolder
            holder.bind(data[position])
        } else {
            holder as MarsViewHolder
            holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].someDescription.isNullOrBlank()) TYPE_MARS else TYPE_EARTH
    }

    inner class EarthViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: Data) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.findViewById<TextView>(R.id.descriptionTextView).text = data.someDescription
                itemView.findViewById<ImageView>(R.id.wikiImageView).setOnClickListener { onListItemClickListener.onItemClick(data) }
            }
        }
    }

    inner class MarsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: Data) {
            itemView.findViewById<ImageView>(R.id.marsImageView).setOnClickListener { onListItemClickListener.onItemClick(data) }
        }
    }

    interface OnListItemClickListener {
        fun onItemClick(data: Data)
    }

    companion object {
        private const val TYPE_EARTH = 0
        private const val TYPE_MARS = 1
    }
}