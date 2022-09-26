package com.hb.jetpack_compose

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.hb.jetpack_compose.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_navigation, R.id.topics_navigation, R.id.settings_navigation
            ), fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            /* println("Destinations home_navigation=${R.id.home_navigation}")
             println("Destinations nav_home=${R.id.nav_home}")
             println("topLevelDestinations id=${topLevelDestinations}")
             println("destination id=${destination.id}")
             println("currentDestination destination id=${controller.currentDestination?.id}")*/
            //这里是分模块导航，如普通的情况不一样
            //val topLevelDestinations = appBarConfiguration.topLevelDestinations
            //这里直接列出顶级的导航
            val topLevelDestinations =
                setOf(R.id.nav_home, R.id.nav_topics, R.id.nav_setting)
            val isTopNav = topLevelDestinations.contains(destination.id)
            binding.mainContent.bottomNavView.isVisible = isTopNav
            binding.appBar.isVisible = isTopNav
        }
        binding.mainContent.bottomNavView.setupWithNavController(navController)

        fullscreen()
    }

    private fun fullscreen() {
        //全屏布局，让内容出现在statusbar和navgationbar后面，但是该flag在Api 30就作废了
        //        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
        //                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        //            window.setDecorFitsSystemWindows(false)
        //        }
        //兼容方案
        //这个到底是什么？
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainContent.bottomNavView) { view, insets ->
            view.updatePadding(bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom)
            return@setOnApplyWindowInsetsListener insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar) { view, insets ->
            view.updatePadding(top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top)
            return@setOnApplyWindowInsetsListener insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}