package com.example.dicodingstory.ui

import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.dicodingstory.R
import com.example.dicodingstory.databinding.ActivityDetailStoriesBinding

class DetailStoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoriesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storiesName = intent.getStringExtra(STORY_NAME)
        val storiesImg = intent.getStringExtra(STORY_IMG)
        val storiesDesc = intent.getStringExtra(STORY_DESC)

        binding.apply {
            Glide.with(this@DetailStoriesActivity)
                .load(storiesImg)
                .into(detailImg)
            detailName.text = storiesName
            detailDescription.text = storiesDesc
        }
        setTitleAndColor(R.color.blue)
    }
    private fun setTitleAndColor(actionBarColorRes: Int) {
        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@DetailStoriesActivity, actionBarColorRes)))
            val titleBar = SpannableString("Detail Story")
            titleBar.setSpan(ForegroundColorSpan(ContextCompat.getColor(this@DetailStoriesActivity, R.color.white)), 0, titleBar.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            titleBar.setSpan(StyleSpan(Typeface.BOLD), 0, titleBar.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            titleBar.setSpan(AbsoluteSizeSpan(24, true), 0, titleBar.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            this.title = titleBar
        }
    }

    companion object {
        const val STORY_NAME = "story_name"
        const val STORY_IMG = "story_ig"
        const val STORY_DESC = "story_desc"
    }
}