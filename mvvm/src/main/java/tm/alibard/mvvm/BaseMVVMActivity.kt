package tm.alibard.mvvm

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager


abstract class BaseMVVMActivity<V : BaseViewModel<*>> : AppCompatActivity() {

    abstract fun onCreteViewModel(): V

    var viewModel: V? = null
        private set
    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}