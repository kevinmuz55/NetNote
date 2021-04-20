package com.unicauca.netnote

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.nav_header_main.*
import android.view.LayoutInflater
import android.view.Menu
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import com.google.firebase.database.ktx.database
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import models.Document
import java.text.SimpleDateFormat
import java.util.*

class PrincipalActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private lateinit var toolbar: Toolbar
    private lateinit var fab: FloatingActionButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var documentID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        auth = FirebaseAuth.getInstance()
        toolbar = findViewById(R.id.toolbar)
        fab = findViewById(R.id.edit_buttom)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            //val database = Firebase.database
            auth = FirebaseAuth.getInstance()
            //val userID = auth.currentUser?.uid
            documentID = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
            //database.getReference("/users/$userID/$documentID/title/").setValue("SinTitulo")
            val intent = Intent(this, EditarNotasActivity::class.java)
            intent.putExtra("documentID", documentID)
            startActivity(intent)
        }

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_share, R.id.nav_paper_bin, R.id.nav_logout, R.id.nav_creditos
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        val user = auth.currentUser
        var textView: TextView

        if (user != null) {
            textView = findViewById(R.id.name_textView)
            textView.text = user.displayName
            textView = findViewById(R.id.email_textView)
            textView.text = user.email
        }

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar,menu)

        val search = menu?.findItem(R.id.search)
        val searchView = search?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

}
