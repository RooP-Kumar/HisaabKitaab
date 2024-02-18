package com.example.hisaabkitaab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.db.entity.MyExpense
import com.example.hisaabkitaab.db.entity.User
import com.example.hisaabkitaab.getAppState
import java.text.SimpleDateFormat
import java.util.Locale

class MyExpenseRecyclerViewAdapter(
    private val context: Context,
) : RecyclerView.Adapter<MyExpenseRecyclerViewAdapter.MyViewHolder>() {

    private val dataList : ArrayList<MyExpense> = arrayListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val amount : TextView = itemView.findViewById(R.id.my_expense_total_amount)
        private val paidBy : TextView = itemView.findViewById(R.id.my_expense_paid_by)
        private val date : TextView = itemView.findViewById(R.id.my_expense_date_field)
        private val itemListRecyclerView : RecyclerView = itemView.findViewById(R.id.my_expense_items_list_recycler_view)
        fun bind(expense: MyExpense) {
            amount.text = context.resources.getString(R.string.rupee, expense.price.toString())
            paidBy.text = (getAppState().user ?: User()).name
            date.text = expense.date?.let {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                    it
                )
            }
            val adapter = MyExpenseItemListRecyclerViewAdapter(context)
            itemListRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager
                .VERTICAL, false)
            itemListRecyclerView.adapter = adapter
            adapter.updateList(expense.items)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout
                .my_expense_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun updateList(newList : List<MyExpense>) {
        dataList.clear()
        dataList.addAll(newList)
        notifyDataSetChanged()
    }
}