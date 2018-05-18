package tm.alibard.mvvm

import android.app.AlertDialog
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.View
import android.widget.Toast
import dagger.android.support.AndroidSupportInjection


abstract class BaseMVVMFragment<V : BaseViewModel<*>> : Fragment(), BaseNavigator {

    abstract fun onCreteViewModel(): V


    var viewModel: V? = null
        set

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