package com.example.hisaabkitaab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.db.entity.Friend
import com.example.hisaabkitaab.db.entity.Transaction
import java.text.DecimalFormat

class TransactionFriendsRecyclerViewAdapter(
    private val context: Context,
    private var transaction: Transaction,
    private val paidByFriends: Double,
    private val onCheckChange : (Transaction, Friend) ->Unit
): RecyclerView
.Adapter<TransactionFriendsRecyclerViewAdapter.MyViewHolder>() {

    private val dataList : ArrayList<Friend> = arrayListOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.transactionFriendNameTV)
        private val checkBox : CheckBox = itemView.findViewById(R.id.isPaidCheckBox)
        private val amount : TextView = itemView.findViewById(R.id.friendRupeeTV)
        fun bind(friend: Friend) {
            val oneAmount : Double = (transaction.amount.toDouble() / dataList.size)
            name.text = friend.name
            if(friend.paid!!) {
                name.setTextColor(context.getColor(R.color.surface))
                amount.setTextColor(context.getColor(R.color.surface))
                checkBox.isChecked = friend.paid!!
                checkBox.isEnabled = !friend.paid!!
                if(friend.name == transaction.paidBy) {
                    val decimalText =
                        DecimalFormat("#.##").format(oneAmount - transaction.amount + paidByFriends)
                    amount.text = context.getString(R.string.rupee, decimalText)
                } else {
                    amount.text = context.getString(R.string.rupee, DecimalFormat("##.##").format(oneAmount))
                }
            } else {
                val paidAmount = oneAmount - friend.howMuchPaid
                if (paidAmount < 0) {
                    name.setTextColor(context.getColor(R.color.surface))
                    amount.setTextColor(context.getColor(R.color.surface))
                    transaction.howManyPaid?.plus(1)
                    friend.paid = true
                    checkBox.isChecked = true
                    checkBox.isEnabled = false
                }
                val decimalText = DecimalFormat("#.##").format(paidAmount)
                amount.text = context.getString(R.string.rupee, decimalText)
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                onCheckChange(transaction, friend.copy(paid = isChecked))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            TransactionFriendsRecyclerViewAdapter.MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout
                    .transaction_friend_item_layout, parent, false))
    }

    override fun onBindViewHolder(
        holder: TransactionFriendsRecyclerViewAdapter.MyViewHolder,
        position: Int
    ) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateList(newList : List<Friend>) {
        dataList.clear()
        dataList.addAll(newList)
        notifyDataSetChanged()
    }
}