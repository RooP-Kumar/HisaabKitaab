package com.example.hisaabkitaab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.ui.utility.fetchAmount
import com.example.hisaabkitaab.ui.utility.fetchTitle
import com.example.hisaabkitaab.ui.utility.fetchType

class AddExpenseItemsRecyclerViewAdapter(
    private val context : Context
) : RecyclerView.Adapter<AddExpenseItemsRecyclerViewAdapter.MyViewHolder>() {

    private val itemList : ArrayList<String> = arrayListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val itemListTitle : TextView = itemView.findViewById(R.id.add_expense_item_list_title)
        private val itemListAmount : TextView = itemView.findViewById(R.id.add_expense_item_list_amount)

        fun bind(value: String) {
            val fetchType = value.fetchType()
            val fetchTitle = value.fetchTitle()
            val text = if (fetchType == null) {
                fetchTitle
            } else {
                "$fetchType : $fetchTitle"
            }
            itemListTitle.text =text
            itemListAmount.text = context.getString(R.string.rupee, value.fetchAmount().toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout
            .add_expense_item_list_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun updateList(newList: List<String>){
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }
}