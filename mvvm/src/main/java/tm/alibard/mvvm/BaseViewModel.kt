package tm.alibard.mvvm

import android.accounts.NetworkErrorException
import android.arch.lifecycle.ViewModel
import android.content.Context
import io.reactivex.disposables.CompositeDisposable

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.internal.functions.Functions


abstract class BaseViewModel<N>(val context: Context) : ViewModel() {
    private val ERROR_CONSUMER: (Throwable) -> Unit = { Functions.ERROR_CONSUMER.accept(it) }
    val compositeDisposable = CompositeDisposable()
    var navigator: N? = null
    protected val isLoadingSubject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    protected fun loading(loading: Boolean) {
        isLoadingSubject.onNext(loading)
    }

    protected fun <T> Observable<T>.startLoading(): Observable<T> {
        return doOnNext { loading(true) }
    }

    protected fun <T> Observable<T>.stopLoading(): Observable<T> {
        return doOnNext { loading(false) }
    }

    protected fun <T> Flowable<T>.startLoading(): Flowable<T> {
        return doOnNext { loading(true) }
    }

    protected fun <T> Flowable<T>.stopLoading(): Flowable<T> {
        return doOnNext { loading(false) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
    fun <T> Single<T>.inteRnetSubsribe(
            onSuccess:(T) ->Unit,
            onError: (Throwable) ->Unit = ERROR_CONSUMER
    ):Disposable{
        if(isNetworkAvailable()){
            throw NetworkErrorException("No Internet")
        }
       return subscribe(onSuccess,onError)
    }



    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
