package tm.alibard.mvvm

import android.app.AlertDialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dagger.android.support.AndroidSupportInjection


abstract class BaseBindFragment<T : ViewDataBinding, V : BaseViewModel<*>> : Fragment(), BaseNavigator {

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


    private var progressDialog: AlertDialog? = null
    override fun onError(it: Throwable) {
        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoad() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            return
        }
        progressDialog = AlertDialog.Builder(context, R.style.AppTheme_ProgressDialog)
                .setView(R.layout.dialog_progress)
                .show()
    }

    override fun hideLoad() {
        progressDialog?.dismiss()
    }
}