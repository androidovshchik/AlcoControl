package com.alcocontrol.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alcocontrol.R
import com.alcocontrol.model.Alcohol
import com.alcocontrol.model.Drink
import com.alcocontrol.view.DashedBox
import kotlinx.android.synthetic.main.header_drink.view.*
import kotlinx.android.synthetic.main.item_drink.view.*
import org.jetbrains.anko.layoutInflater
import java.lang.ref.WeakReference

class DrinkAdapter(listener: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val reference = WeakReference(listener)

    val items = mutableListOf<Any>()

    private var selectedItem: Any? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = viewGroup.context.layoutInflater
        return when (viewType) {
            0 -> HeaderHolder(inflater.inflate(R.layout.header_drink, viewGroup, false))
            else -> ItemHolder(inflater.inflate(R.layout.item_drink, viewGroup, false))
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (viewHolder) {
            is HeaderHolder -> viewHolder.bindView(item as Alcohol)
            is ItemHolder -> viewHolder.bindView(item as Drink)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Alcohol -> 0
            else -> 1
        }
    }

    override fun getItemCount() = items.size

    class HeaderHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = view.tv_title

        fun bindView(item: Alcohol) {
            title.text = item.type
        }
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val name: DashedBox = view.db_drink

        init {
            view.setOnClickListener {
                try {
                    val item = items[adapterPosition]
                    reference.get()?.onDrinkChosen(item as Drink)
                    selectedItem = item
                    notifyDataSetChanged()
                } catch (ignored: Throwable) {
                }
            }
        }

        fun bindView(item: Drink) {
            name.text = item.name
            name.isChecked = item == selectedItem
        }
    }

    interface Listener {

        fun onDrinkChosen(drink: Drink)
    }
}