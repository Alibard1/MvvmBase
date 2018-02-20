package tm.alibard.mvvm


interface BaseNavigator {

    fun onError(it: Throwable)
    fun showLoad()
    fun hideLoad()
}