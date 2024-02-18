package com.example.hisaabkitaab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.db.entity.Friend
import com.example.hisaabkitaab.db.entity.Transaction

class ShowTransactionRecyclerViewAdapter(
    private val context: Context,
    private val onCheckChange: (Friend, Transaction) -> Unit
) : RecyclerView
    .Adapter<ShowTransactionRecyclerViewAdapter.MyViewHolder>() {

    private val dataList : ArrayList<Transaction> = arrayListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title : TextView = itemView.findViewById(R.id.transactionTitleTV)
        private val amount : TextView = itemView.findViewById(R.id.transactionTotalAmountTV)
        private val paidBy : TextView = itemView.findViewById(R.id.paidByTV)
        private val recyclerView : RecyclerView = itemView.findViewById(R.id.transactionItemFriendsRV)
        fun bind(transaction: Transaction) {
            title.text = transaction.title
            amount.text = context.resources.getString(R.string.rupee, transaction.amount.toString())
            paidBy.text = transaction.paidBy
            var paidByFriends = 0.0
            transaction.friends.forEach {
                if(it.name != transaction.paidBy) {
                    paidByFriends += it.howMuchPaid
                }
            }
            val adapter = TransactionFriendsRecyclerViewAdapter(context, transaction, paidByFriends){trans, friend ->
                onCheckChange(friend, transaction.copy(howManyPaid = trans.howManyPaid?.plus(1)))
            }
            recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager
                .VERTICAL, false)
            recyclerView.adapter = adapter
            if(transaction.excludeMe != null && !transaction.excludeMe!!){
                adapter.updateList(transaction.friends)
            } else {
                adapter.updateList(transaction.friends)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout
            .transaction_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun updateList(newList : List<Transaction>) {
        dataList.clear()
        dataList.addAll(newList)
        notifyDataSetChanged()
    }
}