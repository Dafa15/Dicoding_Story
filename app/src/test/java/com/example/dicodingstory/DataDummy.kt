package com.example.dicodingstory

import com.example.dicodingstory.data.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "ini foto",
                "dibuat pada",
                "Tes",
                "ini deskripsi",
                2.4323444,
                5.22345
            )
            items.add(story)
        }
        return items
    }
}