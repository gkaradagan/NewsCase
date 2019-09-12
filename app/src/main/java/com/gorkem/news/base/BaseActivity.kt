package com.gorkem.news.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import com.gorkem.news.R
import com.gorkem.news.base.ui.LoadingDialog
import com.gorkem.news.data.model.ServiceResult
import com.gorkem.news.di.Injectable
import com.gorkem.news.util.ConnectivityUtil
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity<DB : ViewDataBinding, V : BaseViewModel> : AppCompatActivity(),
    Injectable, HasSupportFragmentInjector {

    var loadingDialog: LoadingDialog? = null
    var simpleDialog: AlertDialog? = null

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    lateinit var binding: DB

    @get:LayoutRes
    abstract val layoutRes: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract val vm: V


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        getLifecycle().addObserver(vm)
    }

    override fun onDestroy() {
        getLifecycle().removeObserver(vm)
        onHideLoading()
        hideSimpleDialog()
        super.onDestroy()
    }

    fun onShowLoading() {
        if (loadingDialog == null)
            loadingDialog = LoadingDialog(this)
        loadingDialog!!.show()
    }

    fun onHideLoading() {
        if (loadingDialog != null && loadingDialog!!.isShowing)
            loadingDialog!!.dismiss()
        loadingDialog = null
    }

    private fun hideSimpleDialog() {
        if (simpleDialog != null && simpleDialog!!.isShowing)
            simpleDialog!!.dismiss()
    }

    fun onError(message: String) {
        simpleDialog.whenNull {
            simpleDialog = AlertDialog.Builder(this@BaseActivity)
                .setMessage(message)
                .setPositiveButton(R.string.general_ok) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .setCancelable(true)
                .create()
            simpleDialog!!.setCanceledOnTouchOutside(true)
        }
        simpleDialog!!.show()
    }

    fun <T> call(call: LiveData<ServiceResult<T>>, listen: LoadingResourceImpl<T>) {
        if (!ConnectivityUtil.isInternetAvailable(this)) {
            onError(getString(R.string.not_connected_to_internet))
        } else {
            call.observe(this) { listen.onChanged(it) }
        }
    }
}