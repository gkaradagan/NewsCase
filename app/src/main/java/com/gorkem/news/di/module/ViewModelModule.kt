package com.gorkem.news.di.module

import androidx.lifecycle.ViewModel
import com.gorkem.news.ui.newlist.NewsListViewModel
import com.gorkem.news.di.ViewModelFactory
import com.gorkem.news.di.ViewModelKey
import com.gorkem.news.ui.detail.NewsDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(NewsListViewModel::class)
    abstract fun bindNewsListViewModel(viewModel: NewsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsDetailViewModel::class)
    abstract fun bindNewsDetailViewModel(viewModel: NewsDetailViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelFactory
}
