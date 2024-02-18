package com.example.hisaabkitaab.ui.screen.contact_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hisaabkitaab.adapter.ContactRecyclerViewAdapter
import com.example.hisaabkitaab.databinding.FragmentContactBinding
import com.example.hisaabkitaab.db.entity.Friend
import com.example.hisaabkitaab.ui.utility.Constants
import com.example.hisaabkitaab.ui.utility.Utility


class ContactFragment : Fragment() {
    private lateinit var binding: FragmentContactBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(inflater, container, false)
        mainUI()
        return binding.root
    }

    private fun mainUI() {
        val contacts : ArrayList<Friend> = Utility.getFriendContact(requireActivity()
            .contentResolver, true)
        val temp = contacts.toList()

        binding.contactRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        val adapter = ContactRecyclerViewAdapter(temp as ArrayList<Friend>){
            val bundle = bundleOf(Constants.CONTACT_FRIEND_DATA_KEY to Utility.toJsonString(it))
            setFragmentResult(Constants.CONTACT_FRAGMENT_REQUEST_KEY, bundle)
            findNavController().popBackStack()
        }
        binding.contactRecyclerView.adapter = adapter

        binding.searchBoxEdittext.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                val filterList = contacts.filter {
                    it.name?.lowercase()?.contains(text)!!
                }
                adapter.updateContactList(filterList)
            } else {
                adapter.updateContactList(contacts)
            }
        }

    }
}