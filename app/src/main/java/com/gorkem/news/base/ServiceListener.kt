package com.gorkem.news.base

open class ServiceListener<T> constructor(context: BaseActivity<*, *>) :
    LoadingResourceImpl<T>(context) {
    override fun onSuccess(data: T?) {

    }

}