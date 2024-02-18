package com.example.hisaabkitaab.ui.utility

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.db.entity.Friend
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

object Utility {
    fun getFriendContact(
        contentResolver: ContentResolver,
        sortByName: Boolean = false
    ) : ArrayList<Friend> {
        val friends = HashMap<String, Friend>()
        val cursor : Cursor? = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            null,
            null, null)
        if(cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val friend = Friend()
                val nameIndex = cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone
                        .DISPLAY_NAME
                )
                val phoneIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                friend.name = cursor.getString(nameIndex)
                friend.phone = cursor.getString(phoneIndex)
                if (!friends.containsKey(friend.phone.toString())) friends[friend.phone.toString()] = friend
            }
        }

        cursor?.close()
        val friendsList = friends.values.toList()
        if (sortByName) (friendsList as ArrayList<Friend>).sortWith(ComparatorByName())
        return friendsList as ArrayList<Friend>
    }

    fun toJsonString(clazz : Any) : String {
        val gson = Gson()
        return gson.toJson(clazz)
    }

    fun <T> fromJsonString(jsonString: String, clazz: Class<T>) : T {
        val gson = Gson()
        return gson.fromJson(jsonString, clazz)
    }

    fun readyBottomBar(requireActivity: Activity, requireContext: Context) {
        val totalAmountText = requireActivity.findViewById<TextView>(R.id.balance_total_price)
        val bottomNavigationView = requireActivity.findViewById<BottomNavigationView>(
            R.id
            .bottomNavigationBar)
        bottomNavigationView.itemActiveIndicatorColor = ContextCompat.getColorStateList(requireContext
            , R.color.transparent_back)

        bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(requireContext, R
            .color.bottom_navigation_color_list)

        bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(requireContext, R
            .color.bottom_navigation_color_list)

        val toolbar = requireActivity.findViewById<Toolbar>(R.id.toolbar)

        val configuration = AppBarConfiguration(
            setOf(
                R.id.home,
                R.id.my_expense,
                R.id.setting
            )
        )

        try {
            val navController = requireActivity.findNavController(R.id.fragmentContainerView)
            navController.addOnDestinationChangedListener(){ _, destination, _ ->
                when (destination.id) {
                    R.id.home -> {
                        toolbar.visibility = View.VISIBLE
                        bottomNavigationView.visibility = View.VISIBLE
                        totalAmountText.visibility = View.GONE
                    }
                    R.id.my_expense -> {
                        toolbar.visibility = View.VISIBLE
                        bottomNavigationView.visibility = View.VISIBLE
                        totalAmountText.visibility = View.VISIBLE
                    }
                    R.id.setting -> {
                        toolbar.visibility = View.VISIBLE
                        bottomNavigationView.visibility = View.VISIBLE
                    }
                    R.id.signUpFragment -> {
                        toolbar.visibility = View.GONE
                        bottomNavigationView.visibility = View.GONE
                    }
                    R.id.signInFragment -> {
                        toolbar.visibility = View.GONE
                        bottomNavigationView.visibility = View.GONE
                    }
                    else -> {
                        toolbar.visibility = View.VISIBLE
                        bottomNavigationView.visibility = View.GONE
                        totalAmountText.visibility = View.GONE
                    }
                }
            }
            bottomNavigationView.setupWithNavController(navController)
            toolbar.setupWithNavController(navController, configuration)
        } catch (e: Exception) {
            Toast.makeText(requireContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}

class ComparatorByName : Comparator<Friend> {
    override fun compare(o1: Friend?, o2: Friend?): Int {
        if(o1 == null || o2 == null) {
            return 0
        }

        return o2.name?.let { o1.name?.compareTo(it) }!!
    }

}