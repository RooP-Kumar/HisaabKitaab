package com.example.hisaabkitaab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.db.entity.Friend

class ContactRecyclerViewAdapter(
    private val contactList: ArrayList<Friend>,
    private val onItemClick : (Friend) -> Unit
): RecyclerView
    .Adapter<ContactRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameTV: TextView = itemView.findViewById(R.id.contactNameTV)
        private val phoneTV: TextView = itemView.findViewById(R.id.contactPhoneTV)
        private val mainLL : LinearLayout = itemView.findViewById(R.id.mainContentLL)

        fun bind(contact : Friend) {
            nameTV.text = contact.name
            phoneTV.text = contact.phone
            mainLL.setOnClickListener {
                onItemClick(contact)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout
            .contact_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(contact = contactList[position])
    }

    fun updateContactList(newList : List<Friend>) {
        contactList.clear()
        contactList.addAll(newList)
        notifyDataSetChanged()
    }

}