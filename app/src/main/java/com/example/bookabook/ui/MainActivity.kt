package com.example.bookabook.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookabook.R
import com.example.bookabook.databinding.ActivityMainBinding
import com.example.bookabook.model.fcm.FireBaseCloudMessage
import com.example.bookabook.utils.NEW_BOOK_TOPIC
import com.example.bookabook.utils.NetworkConnection
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {
                binding.connectionStatusLinearLayout.visibility = View.GONE
            } else {
                binding.connectionStatusLinearLayout.visibility = View.VISIBLE
            }
        })



        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        val navController = host.navController


        appBarConfiguration = AppBarConfiguration(navController.graph)


        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.profileFragment,
                R.id.favouriteBooks,
                R.id.myBooks,
                R.id.notificationFragment,
                R.id.aboutFragment
            ),
            drawerLayout
        )

        setupNavigationMenu(navController)

        setupActionBar(navController, appBarConfiguration)

        subscribeToTopic()

        //    setupBottomNavMenu(navController)


    }

    private fun subscribeToTopic() {

        FirebaseMessaging.getInstance().subscribeToTopic(NEW_BOOK_TOPIC)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.home_menu, menu)
//
//        val searchManager =
//            getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        (menu!!.findItem(R.id.homeSearch).actionView as SearchView).apply {
//            setSearchableInfo(searchManager.getSearchableInfo(componentName))
//            isQueryRefinementEnabled = true
//        }
//
//        return true
//
//    }

//    private fun setupBottomNavMenu(navController: NavController) {
//        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
//        bottomNav?.setupWithNavController(navController)
//    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupNavigationMenu(navController: NavController) {

        val sideNavView = findViewById<NavigationView>(R.id.nav_view)

        sideNavView?.setupWithNavController(navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration)
    }


}