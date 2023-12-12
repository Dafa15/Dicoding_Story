package com.example.dicodingstory.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingstory.data.response.ListStoryItem
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.dicodingstory.R
import com.example.dicodingstory.ui.DetailStoriesActivity


class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_stories, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imgPhoto: ImageView = itemView.findViewById(R.id.img_story)
        private var tvName: TextView = itemView.findViewById(R.id.story_name)
        private var tvDescription: TextView = itemView.findViewById(R.id.story_description)
        @SuppressLint("SuspiciousIndentation")
        fun bind(story: ListStoryItem) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(imgPhoto)
            tvName.text = story.name
            tvDescription.text = story.description

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoriesActivity::class.java)
                intent.putExtra(DetailStoriesActivity.STORY_NAME, story.name)
                intent.putExtra(DetailStoriesActivity.STORY_IMG, story.photoUrl)
                intent.putExtra(DetailStoriesActivity.STORY_DESC, story.description)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(imgPhoto, "animation_image"),
                        Pair(tvName, "animation_name"),
                        Pair(tvDescription, "animation_desc"),
                    )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}