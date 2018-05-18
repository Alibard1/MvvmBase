package tm.alibard.mvvm

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import dagger.android.AndroidInjection


abstract class BaseBindActivity<T : ViewDataBinding, V : BaseViewModel<*>> : BaseMVVMActivity<V>() {

    var mViewDataBinding: T? = null
        private set


    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingVariable(): Int


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        mViewDataBinding?.setVariable(getBindingVariable(), viewModel)
        mViewDataBinding?.executePendingBindings()
    }
}