package com.example.adiblar_hayoti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.example.adiblar_hayoti.databinding.ActivityHolderBinding
import me.ibrahimsn.lib.OnItemSelectedListener

class HolderActivity : AppCompatActivity() {
    lateinit var binding: ActivityHolderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHolderBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupSmoothBottomMenu()


    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_menu)
        val menu = popupMenu.menu
        val navController = findNavController(R.id.asad)
        binding.bottomBar.setupWithNavController(menu, navController)
    }

    fun showBottomNavigation()
    {
        binding.bottomBar.visibility = View.VISIBLE



    }

    fun hideBottomNavigation()
    {
        binding.bottomBar.visibility = View.GONE
        val fragmenttt = findViewById<View>(R.id.asad)
    }
}