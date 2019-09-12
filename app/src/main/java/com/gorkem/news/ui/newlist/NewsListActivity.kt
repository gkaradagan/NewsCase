package com.gorkem.news.ui.newlist

import android.os.Bundle
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gorkem.news.R
import com.gorkem.news.base.BaseActivity
import com.gorkem.news.base.LoadingResourceImpl
import com.gorkem.news.base.launchActivity
import com.gorkem.news.data.model.Articles
import com.gorkem.news.databinding.ActivityNewsListBinding
import com.gorkem.news.ui.detail.NewsDetailActivity
import com.gorkem.news.ui.detail.NewsDetailActivity.Companion.DETAIL_TITLE
import com.gorkem.news.ui.detail.NewsDetailActivity.Companion.DETAIL_URL
import com.gorkem.news.util.DateUtil
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.util.*
import javax.inject.Inject


class NewsListActivity : BaseActivity<ActivityNewsListBinding, NewsListViewModel>(),
    DatePickerDialog.OnDateSetListener {

    private lateinit var adapter: NewsListAdapter
    @Inject
    lateinit var viewModel: NewsListViewModel

    private var calendar = Calendar.getInstance()
    private var currentMonth: Int = 0
    private var currentYear: Int = 0
    private var currentDay: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        currentYear = calendar.get(Calendar.YEAR)
        currentMonth = calendar.get(Calendar.MONTH)
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        binding.dateFilter.setOnClickListener {
            val datePickerDialog: DatePickerDialog = DatePickerDialog.newInstance(
                this@NewsListActivity,
                currentYear,
                currentMonth,
                currentDay
            )
            datePickerDialog.apply {
                isThemeDark = false
                showYearPickerFirst(false)
                title = this@NewsListActivity.getString(R.string.date_picker_title)
            }
            datePickerDialog.show(supportFragmentManager, "DatePickerDialog")
        }

        binding.recyclerView.apply {
            var linearLayoutManager = LinearLayoutManager(this@NewsListActivity)
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
        }

        adapter = NewsListAdapter()
        binding.recyclerView.adapter = adapter
        adapter.listen().observe(this) {
            val article = adapter.getItems()[it]
            launchActivity<NewsDetailActivity> {
                putExtra(DETAIL_TITLE, article.title)
                putExtra(DETAIL_URL, article.url)
            }
        }

        call(viewModel.getArticles(), object : LoadingResourceImpl<Articles>(this) {
            override fun onSuccess(data: Articles?) {
                adapter.clearItems()
                adapter.addItems(data!!.articles)
            }
        })
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val to = DateUtil.createDateString(year, monthOfYear, dayOfMonth)
        val fromCal = DateUtil.createDate(year, monthOfYear, dayOfMonth)
        val from = DateUtil.addDay(fromCal, -10)
        call(
            viewModel.getArticles(from = from, to = to),
            object : LoadingResourceImpl<Articles>(this) {
                override fun onSuccess(data: Articles?) {
                    adapter.clearItems()
                    adapter.addItems(data!!.articles)
                }
            })
    }

    override val layoutRes: Int
        get() = R.layout.activity_news_list
    override val vm: NewsListViewModel
        get() = viewModel
}
