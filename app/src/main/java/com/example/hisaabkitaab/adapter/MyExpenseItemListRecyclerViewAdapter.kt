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

class MyExpenseItemListRecyclerViewAdapter(
    private val context: Context
): RecyclerView
.Adapter<MyExpenseItemListRecyclerViewAdapter.MyViewHolder>() {
    private val dataList : ArrayList<String> = arrayListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.my_expense_items_list_itemsNameTV)
        private val amount : TextView = itemView.findViewById(R.id.my_expense_items_list_itemsRupeeTV)
        fun bind(value: String) {
            val temp = "${ value.fetchType() ?: "" } ${value.fetchTitle()}"
            name.text = temp
            amount.text = context.getString(R.string.rupee, value.fetchAmount().toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MyExpenseItemListRecyclerViewAdapter.MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.my_expense_item_layout_items_layout, parent, false))
    }

    override fun onBindViewHolder(
        holder: MyExpenseItemListRecyclerViewAdapter.MyViewHolder,
        position: Int
    ) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateList(newList : List<String>) {
        dataList.clear()
        dataList.addAll(newList)
        notifyDataSetChanged()
    }
}