package com.gorkem.news.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gorkem.news.data.model.ServiceResult
import kotlinx.coroutines.*

open class BaseViewModel : ViewModel(), LifecycleObserver {
    /**
     * This is the job for all coroutines started by this ViewModel.
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    protected val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     * Since we pass viewModelJob, you can cancel all coroutines
     * launched by uiScope by calling viewModelJob.cancel()
     */
    protected val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

   protected fun <T> call(networkCall: suspend () -> ServiceResult<T>): LiveData<ServiceResult<T>> {
        val responseLiveData: MutableLiveData<ServiceResult<T>> = MutableLiveData()
        uiScope.launch {
            responseLiveData.value = ServiceResult.loading()
            val task = withContext(Dispatchers.Main) {
                // background thread
                networkCall.invoke()
            }
            responseLiveData.value = task
        }
        return responseLiveData
    }
}