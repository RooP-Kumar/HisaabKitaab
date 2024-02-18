package com.example.hisaabkitaab.ui.screen.add_transaction

import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.adapter.AddFriendRecyclerViewAdapter
import com.example.hisaabkitaab.adapter.PaidByDialogListAdapter
import com.example.hisaabkitaab.databinding.FragmentAddTransactionDialogBinding
import com.example.hisaabkitaab.db.entity.Friend
import com.example.hisaabkitaab.db.entity.Transaction
import com.example.hisaabkitaab.getAppState
import com.example.hisaabkitaab.ui.utility.Constants
import com.example.hisaabkitaab.ui.utility.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTransactionFragment : Fragment() {
    private lateinit var binding : FragmentAddTransactionDialogBinding
    private var requestLauncher : ActivityResultLauncher<String>? = null
    private val viewModel : AddTransactionViewModel by viewModels()
    private var addFriendRecyclerViewAdapter : AddFriendRecyclerViewAdapter? =
        null

    private var paidByDialogListAdapter : PaidByDialogListAdapter? = null

    private var paidByDialog : Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_transaction_dialog,
            container, false)
        requestLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            it?.let {
                if(it) {
                    findNavController().navigate(R.id.action_addTransactionDialogFragment_to_contactFragment)
                }
            }
        }
        parentFragmentManager.setFragmentResultListener(Constants.CONTACT_FRAGMENT_REQUEST_KEY, requireActivity())
        {_, result ->

            val friend = Utility.fromJsonString(result.getString(Constants.CONTACT_FRIEND_DATA_KEY)!!, Friend::class.java)
            viewModel.addInTotalAddFriend(friend = friend)
            viewModel.addInPaidByFriendList(null, friend)
        }
        mainUI()
        return binding.root
    }

    private fun mainUI() {

        binding.addFriendBtn.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                findNavController().navigate(R.id.action_addTransactionDialogFragment_to_contactFragment)
            } else {
                requestLauncher?.launch(android.Manifest.permission.READ_CONTACTS)
            }
        }

        viewModel.totalAddFriend.observe(viewLifecycleOwner) { totalAddFriend ->
            if (totalAddFriend?.isNotEmpty() == true) {
                binding.noFriendsTV.visibility = View.GONE
                binding.friendsRecyclerView.visibility = View.VISIBLE
                val temp : HashMap<String, Friend> = hashMapOf<String, Friend>().apply {
                    totalAddFriend.forEach { friend -> this[friend.phone.toString()] = friend }
                }
                addFriendRecyclerViewAdapter?.updateList(temp)
            }

            binding.excludeMeCheckBox.isEnabled = totalAddFriend?.size!! >= 2

            enableOrDisableExcludeMe()
        }

        viewModel.paidByFriendList.observe(viewLifecycleOwner) {
            it?.let {friends ->
                if (friends.size == 1) {
                    binding.paidByEditText.setText(friends[getAppState().user!!.phone.toString()]?.name)
                } else {
                    paidByDialogListAdapter?.updateList(friends.values.toList() as ArrayList<Friend>)
                }
            }
        }

        paidByDialogListAdapter = PaidByDialogListAdapter(arrayListOf()){
            val temp = binding.paidByEditText.text?.toString()
            viewModel.paidByFriendList.value?.let { friendsMap ->
                friendsMap.forEach loop@ { _, friend ->
                    if(temp == friend.name){
                        viewModel.addInTotalAddFriend(friend = friend)
                        friend.paid = false
                        return@loop
                    }
                }
            }
            binding.paidByEditText.setText(it.name)
            it.paid = true
            viewModel.deleteFromTotalAddFriend(it)
            enableOrDisableExcludeMe()
            paidByDialog?.dismiss()
        }

        binding.paidByEditText.setOnClickListener {
            paidByDialog = Dialog(requireContext())
            paidByDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            paidByDialog?.setContentView(R.layout.fragment_paid_by_dialog)

            val rv = paidByDialog?.findViewById<RecyclerView>(R.id.paid_by_recycler_view)
            val tv = paidByDialog?.findViewById<TextView>(R.id.no_friend_msg)
            if(viewModel.paidByFriendList.value?.size == 1) {
                tv?.visibility = View.VISIBLE
            } else {
                tv?.visibility = View.GONE
            }
            rv?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager
                .VERTICAL, false)
            paidByDialogListAdapter?.let { rv?.adapter = it }
            paidByDialog?.show()
        }

        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager
            .VERTICAL, false)
        addFriendRecyclerViewAdapter = AddFriendRecyclerViewAdapter(hashMapOf()){
            // Delete Function
        }
        binding.friendsRecyclerView.adapter = addFriendRecyclerViewAdapter

        binding.addTransactionBtn.setOnClickListener {
            val title : String = binding.titleEditText.text.toString()
            val amount : String = binding.amountEdittext.text.toString()
            val paidBy : String = binding.paidByEditText.text.toString()
            val excludeMe : Boolean = binding.excludeMeCheckBox.isChecked
            if(title.isEmpty()) {
                binding.titleEditText.error = "Title should not empty"
            } else if(amount.isEmpty()) {
                binding.amountEdittext.error = "Amount should not empty"
            } else if(paidBy.isEmpty()){
                binding.paidByEditText.error = "Paid by should not empty"
            } else if (viewModel.paidByFriendList.value?.size!! < 2) {
                Toast.makeText(requireContext(), "Please add at least one friend", Toast
                    .LENGTH_SHORT).show()
            } else {
                val temp = viewModel.paidByFriendList.value
                if(temp != null) {
                    var paidByFriend = ""
                    var distributedAmountByFriend = 0.0
                    temp.forEach { (key, friend) ->
                        if(friend.name == paidBy){
                            paidByFriend = key
                        }
                        distributedAmountByFriend += friend.howMuchPaid
                    }

                    temp[paidByFriend]?.howMuchPaid = (amount.toLong() - distributedAmountByFriend)

                    if (excludeMe) {
                        temp.remove(getAppState().user!!.phone.toString())
                    }

                    val transaction = Transaction(
                        title = title,
                        amount = amount.toLong(),
                        friends = temp.values.toList(),
                        fullyPaid = false,
                        paidBy = paidBy,
                        excludeMe = excludeMe,
                        howManyPaid = 1
                    )
                    viewModel.addTransaction(transaction)
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun enableOrDisableExcludeMe() {
        val paidBy : String = binding.paidByEditText.text.toString()
        if(paidBy == getAppState().user!!.name) {
            binding.excludeMeCheckBox.isChecked = false
            binding.excludeMeCheckBox.isEnabled = false
            binding.transactionExludeMeTv.setTextColor(requireContext().getColor(R.color.surface))
        } else {
            binding.excludeMeCheckBox.isEnabled = true
            binding.transactionExludeMeTv.setTextColor(requireContext().getColor(R.color.onBackground))
        }
    }

}