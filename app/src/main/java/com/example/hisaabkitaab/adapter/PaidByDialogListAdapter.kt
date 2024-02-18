package com.example.hisaabkitaab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.db.entity.Friend
import com.google.android.material.textfield.TextInputLayout

class PaidByDialogListAdapter(
    private var paidByList: ArrayList<Friend>,
    private val onItemClick : (Friend) -> Unit
): RecyclerView.Adapter<PaidByDialogListAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTV: TextView = itemView.findViewById(R.id.friendName)
        private val friendAmountLayout: TextInputLayout = itemView.findViewById(R.id
            .add_friend_amount_layout)
        private val friendLayout : LinearLayout = itemView.findViewById(R.id.friendLayoutLL)

        fun bind(friend : Friend) {
            friendAmountLayout.visibility = View.GONE
            nameTV.text = friend.name
            friendLayout.setOnClickListener {
                onItemClick(friend)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout
            .add_friend_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return paidByList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(friend = paidByList[position])
    }

    fun updateList(friends: ArrayList<Friend>){
        paidByList.clear()
        paidByList.addAll(friends)
        notifyDataSetChanged()
    }

}