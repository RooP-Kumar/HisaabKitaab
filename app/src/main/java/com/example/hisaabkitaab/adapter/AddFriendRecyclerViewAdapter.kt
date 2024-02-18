package com.example.hisaabkitaab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.db.entity.Friend

class AddFriendRecyclerViewAdapter(
    private var friendList: HashMap<String, Friend>,
    private val onDeleteClick : (friend: Friend) -> Unit
) : RecyclerView
    .Adapter<AddFriendRecyclerViewAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTV : TextView = itemView.findViewById(R.id.friendName)
        private val amountTv : TextView = itemView.findViewById(R.id.add_friend_amount)
        private val friendLL : LinearLayout = itemView.findViewById(R.id.friendLayoutLL)

        fun bind(item: Friend) {
            nameTV.text = item.name
            amountTv.hint = "0.0"
            amountTv.doOnTextChanged { text, _, _, _ ->
                if(text?.isEmpty() == true) {
                    item.howMuchPaid = 0.0
                } else {
                    item.howMuchPaid = text.toString().toDouble()
                }
                friendList[item.phone.toString()] = item
            }
            friendLL.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_friend_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(friendList.values.toList()[position])
    }

    fun updateList(newList : HashMap<String, Friend>) {
        friendList = newList
        notifyDataSetChanged()
    }
}