package com.gorkem.news.ui.newlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gorkem.news.base.LiveEvent
import com.gorkem.news.data.model.Article
import com.gorkem.news.databinding.NewsListItemRowBinding

class NewsListAdapter : RecyclerView.Adapter<NewsListAdapter.NewsListViewHolder>() {

    private val listen = LiveEvent<Int>()
    private val itemList = mutableListOf<Article>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        val binding =
            NewsListItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsListViewHolder(binding,listen)
    }

    fun listen(): LiveEvent<Int> = listen


    fun addItems(list: List<Article>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearItems() {
        itemList.clear()
    }

    fun getItems() = itemList

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        val data = itemList.get(position)
        holder.bind(data)
    }

    class NewsListViewHolder(var binding: NewsListItemRowBinding, val listen: LiveEvent<Int>) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listen.value = adapterPosition
            }
        }

        fun bind(data: Article) {
            binding.item = data
        }
    }
}