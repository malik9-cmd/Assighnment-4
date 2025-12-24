package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.adapter.PostAdapter
import com.example.androidproject.data.DatabaseHelper
import com.example.androidproject.data.Post
import com.example.androidproject.network.ApiService
import com.example.androidproject.utils.PrefsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var prefsManager: PrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        prefsManager = PrefsManager(this)
        applySavedTheme()
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        tvEmpty = findViewById(R.id.tvEmpty)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostAdapter(emptyList()) { post ->
            // On Item Click -> Open WebView with a dummy URL related to the post
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra(WebViewActivity.EXTRA_URL, "https://jsonplaceholder.typicode.com/posts/${post.id}")
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        fetchPosts()
    }
    
    private fun applySavedTheme() {
        when (prefsManager.selectedTheme) {
            PrefsManager.THEME_LIGHT -> setTheme(R.style.Theme_AndroidProject_Light)
            PrefsManager.THEME_DARK -> setTheme(R.style.Theme_AndroidProject_Dark)
            PrefsManager.THEME_CUSTOM -> setTheme(R.style.Theme_AndroidProject_Custom)
            else -> setTheme(R.style.Theme_AndroidProject_Light)
        }
    }

    private fun fetchPosts() {
        progressBar.visibility = View.VISIBLE
        
        // Check API
        val apiService = ApiService.create()
        apiService.getPosts().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful && response.body() != null) {
                    val posts = response.body()!!
                    
                    // Save to DB in background
                    CoroutineScope(Dispatchers.IO).launch {
                        // Clear old data might be too aggressive, let's just insert/update
                        // databaseHelper.clearAll() // If implemented
                        posts.forEach { databaseHelper.insertPost(it) }
                        
                        withContext(Dispatchers.Main) {
                            adapter.updateData(posts)
                            progressBar.visibility = View.GONE
                            tvEmpty.visibility = View.GONE
                        }
                    }
                } else {
                    loadFromDb(true)
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
               loadFromDb(true)
            }
        })
    }
    
    private fun loadFromDb(showError: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val posts = databaseHelper.getAllPosts()
             withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
                if (posts.isNotEmpty()) {
                    adapter.updateData(posts)
                    tvEmpty.visibility = View.GONE
                    if(showError) Toast.makeText(this@MainActivity, R.string.no_internet, Toast.LENGTH_SHORT).show()
                } else {
                     tvEmpty.visibility = View.VISIBLE
                     if(showError) Toast.makeText(this@MainActivity, R.string.fetch_error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                prefsManager.isLoggedIn = false
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            R.id.theme_light -> {
                updateTheme(PrefsManager.THEME_LIGHT)
                true
            }
            R.id.theme_dark -> {
                updateTheme(PrefsManager.THEME_DARK)
                true
            }
            R.id.theme_custom -> {
                 updateTheme(PrefsManager.THEME_CUSTOM)
                true
            }
             else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun updateTheme(theme: Int) {
        if (prefsManager.selectedTheme != theme) {
            prefsManager.selectedTheme = theme
            recreate()
        }
    }
}
