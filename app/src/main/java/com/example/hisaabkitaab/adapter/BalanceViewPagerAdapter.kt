package com.example.hisaabkitaab.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hisaabkitaab.ui.screen.balance.BalanceFragment

class BalanceViewPagerAdapter(fm : FragmentActivity): FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return BalanceFragment()
    }

}