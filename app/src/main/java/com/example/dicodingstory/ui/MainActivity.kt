package com.example.dicodingstory.ui

import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingstory.R
import com.example.dicodingstory.adapter.LoadingStateAdapter
import com.example.dicodingstory.adapter.StoryAdapter
import com.example.dicodingstory.data.response.ListStoryItem
import com.example.dicodingstory.databinding.ActivityMainBinding
import com.example.dicodingstory.ui.addstory.AddStoryActivity
import com.example.dicodingstory.viewmodel.AuthViewModel
import com.example.dicodingstory.viewmodel.StoryViewModel
import com.example.dicodingstory.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val storyViewModel by viewModels<StoryViewModel>{
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addStory.setOnClickListener{
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
        getStories()
        setTitleAndColor(R.color.blue)
    }

    override fun onResume() {
        super.onResume()
        setupAction()
    }

    private fun setupAction() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager =layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStories.addItemDecoration(itemDecoration)
    }

    private fun getStories() {
        val adapter = StoryAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        storyViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.clearToken()
                }
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            R.id.maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTitleAndColor(actionBarColorRes: Int) {
        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MainActivity, actionBarColorRes)))
            val titleBar = SpannableString("The Story")
            titleBar.setSpan(ForegroundColorSpan(ContextCompat.getColor(this@MainActivity, R.color.white)), 0, titleBar.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            titleBar.setSpan(StyleSpan(Typeface.BOLD), 0, titleBar.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            titleBar.setSpan(AbsoluteSizeSpan(24, true), 0, titleBar.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            this.title = titleBar
        }
    }

}