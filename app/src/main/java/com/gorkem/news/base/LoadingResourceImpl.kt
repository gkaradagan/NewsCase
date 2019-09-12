package com.gorkem.news.base

import androidx.lifecycle.Observer
import com.gorkem.news.R
import com.gorkem.news.data.model.ServiceResult

abstract class LoadingResourceImpl<T>(private var context: BaseActivity<*, *>) :
    Observer<ServiceResult<T>>, LoadingResource<T> {

    override fun onChanged(result: ServiceResult<T>) {
        when (result.status) {
            ServiceResult.Status.SUCCESS -> {
                onHideLoading(true)
                onSuccess(result.data)
            }
            ServiceResult.Status.LOADING -> {
                onShowLoading()
            }
            ServiceResult.Status.ERROR -> {
                onHideLoading(false)
                onError(result.message)
            }
        }
    }

    override fun onError(message: String?) {
        if (!context.isFinishing)
            context.onError(message ?: context.getString(R.string.general_error))
    }

    override fun onShowLoading() {
        if (!context.isFinishing)
            context.onShowLoading()
    }

    override fun onHideLoading(isSucces: Boolean) {
        if (!context.isFinishing)
            context.onHideLoading()
    }
}