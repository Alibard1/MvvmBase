package tm.alibard.mvvm

import android.arch.lifecycle.ViewModel


abstract class BaseViewModel<N> : ViewModel() {

    var navigator: N? = null

}
