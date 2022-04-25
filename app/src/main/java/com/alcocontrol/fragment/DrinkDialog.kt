package com.alcocontrol.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.alcocontrol.R
import com.alcocontrol.activity.MainViewModel
import com.alcocontrol.adapter.DrinkAdapter
import com.alcocontrol.data.Preferences
import com.alcocontrol.model.Drink
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_drink.*
import kotlinx.android.synthetic.main.include_close.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DrinkDialog : SheetDialog(), DrinkAdapter.Listener {

    @Inject
    lateinit var preferences: Preferences

    private val viewModel: MainViewModel by activityViewModels()

    private val adapter = DrinkAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, root: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_drink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val profile = preferences.profile!!
        tv_title.text = getString(R.string.drink_question, profile.name)
        fab_close.setOnClickListener {
            dismiss()
        }
        il_search.addTextChangedListener {
            updateList()
        }
        rv_drinks.adapter = adapter
        tv_choose.setOnClickListener {
            dismiss()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.alcoholDrinks.collect {
                updateList()
            }
        }
        if (viewModel.drink.value != null) {
            enableButton()
        }
    }

    private fun updateList() {
        val query = il_search.text.toString().trim()
        adapter.items.clear()
        viewModel.alcoholDrinks.value.forEach { (type, drinks) ->
            val items = drinks.filter { it.name.contains(query, true) }
            if (items.isNotEmpty()) {
                adapter.items.add(type)
                adapter.items.addAll(items)
            }
        }
        adapter.notifyDataSetChanged()
        if (adapter.items.isEmpty()) {
            tv_not_found.text = getString(R.string.drink_not_found, query)
            tv_not_found.isVisible = true
        } else {
            tv_not_found.isVisible = false
        }
    }

    override fun onDrinkChosen(drink: Drink) {
        viewModel.drink.value = drink
        enableButton()
    }

    private fun enableButton() {
        tv_choose.apply {
            backgroundTintList = null
            setTextColor(Color.WHITE)
            isEnabled = true
        }
    }

    companion object : Instance<DrinkDialog>(DrinkDialog::class.java)
}