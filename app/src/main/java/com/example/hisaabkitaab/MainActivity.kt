package com.example.hisaabkitaab

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.core.content.getSystemService
import androidx.navigation.findNavController
import com.example.hisaabkitaab.databinding.ActivityMainBinding
import com.example.hisaabkitaab.db.dao.UserDao
import com.example.hisaabkitaab.repository.MyExpenseRepository
import com.example.hisaabkitaab.ui.utility.Utility
import com.example.hisaabkitaab.ui.utility.io
import com.example.hisaabkitaab.ui.utility.main
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    @Inject
    lateinit var repository: MyExpenseRepository

    @Inject
    lateinit var userDao : UserDao

    companion object {
        const val TAG = "main activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        io {

            repository.getUnsyncedExpenses().collectLatest {
                
                if(it.isNotEmpty()){
                    window.statusBarColor = getColor(R.color.status_bar_color_when_not_sync)
                } else {
                    window.statusBarColor = getColor(R.color.status_bar_color)
                }

            }

        }

    }

    override fun onStart() {

        io {

            userDao.getUser().collectLatest {

                getAppState().user = it

            }

        }

        super.onStart()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        Utility.readyBottomBar(this, this)

        return true

    }
}