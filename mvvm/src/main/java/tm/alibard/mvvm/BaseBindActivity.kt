package tm.alibard.mvvm

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import dagger.android.AndroidInjection


abstract class BaseBindActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity() {
    abstract fun onCreteViewModel(): V

    var mViewDataBinding: T? = null
        private set

    var viewModel: V? = null
        private set

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

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

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}