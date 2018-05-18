package tm.alibard.mvvm

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection


abstract class BaseBindFragment<T : ViewDataBinding, V : BaseViewModel<*>> : BaseMVVMFragment<V>(), BaseNavigator {



    var mViewDataBinding: T? = null
        private set
    private var mRootView: View? = null

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = onCreteViewModel()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = mViewDataBinding?.root
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        performDataBinding()
    }

    private fun performDataBinding() {
        mViewDataBinding?.setVariable(getBindingVariable(), viewModel)
        mViewDataBinding?.executePendingBindings()
    }
    abstract fun getBindingVariable(): Int
}