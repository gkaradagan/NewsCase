package com.gorkem.news.di.module

import com.gorkem.news.ui.detail.NewsDetailActivity
import com.gorkem.news.ui.newlist.NewsListActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeNewsListActivity(): NewsListActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeNewsDetailActivity(): NewsDetailActivity
}
