package com.gorkem.news.ui.detail

import android.os.Bundle
import com.gorkem.news.R
import com.gorkem.news.base.BaseActivity
import com.gorkem.news.databinding.ActivityNewsDetailBinding
import javax.inject.Inject

class NewsDetailActivity : BaseActivity<ActivityNewsDetailBinding, NewsDetailViewModel>() {
    @Inject
    lateinit var viewModel: NewsDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val url = intent?.extras?.getString(DETAIL_URL)
        val title = intent?.extras?.getString(DETAIL_TITLE)

        supportActionBar!!.title = title
        binding.webview.loadUrl(url)

        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    override val layoutRes: Int
        get() = R.layout.activity_news_detail
    override val vm: NewsDetailViewModel
        get() = viewModel

    companion object {
        const val DETAIL_URL = "DETAIL_URL"
        const val DETAIL_TITLE = "DETAIL_TITLE"
    }
}