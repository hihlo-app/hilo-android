package com.app.hihlo.ui.profile.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.app.hihlo.R
import com.app.hihlo.model.interest_list.response.Interests

class CustomSpinnerAdapter(
    private val context: Context,
    private val items: List<Interests>
) : ArrayAdapter<Interests>(context, R.layout.spinner_edit_profile, items) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Selected item view
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Drop down view
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_edit_profile, parent, false)
        val textView = view.findViewById<TextView>(R.id.spinnerItem)
        textView.text = items[position].name
        Log.i("TAG", "createItemView: "+items)
        return view
    }
}
