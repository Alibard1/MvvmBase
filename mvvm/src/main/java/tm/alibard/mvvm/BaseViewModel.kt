package tm.alibard.mvvm

import android.accounts.NetworkErrorException
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.net.ConnectivityManager
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.functions.Functions
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList


abstract class BaseViewModel<N>(context: Context) : ViewModel() {
    private var weekReference: WeakReference<Context> = WeakReference(context)
    private val ERROR_CONSUMER: (Throwable) -> Unit = { Functions.ERROR_CONSUMER.accept(it) }
    private val compositeDisposable = CompositeDisposable()
    var navigator: N? = null
    val emptyDisposable = object : Disposable {
        override fun isDisposed(): Boolean = true

        override fun dispose() {

        }

    }
    val taskTimer = Timer()
    private var pendingActions = CopyOnWriteArrayList<() -> Unit>()

    init {

        taskTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (isNetworkAvailable()) {
                    pendingActions.forEach { it() }
                    pendingActions.clear()
                }
            }

        },
                //Set how long before to start calling the TimerTask (in milliseconds)
                0,
                //Set the amount of time between each execution (in milliseconds)
                3000)
    }


    private val isLoadingSubject: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    private fun loading(loading: Boolean) {
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
        taskTimer.cancel()
    }

    fun <T> Single<T>.subScribeWithNetwork(
            onSuccess: (T) -> Unit,
            onError: (Throwable) -> Unit = ERROR_CONSUMER
    ): Disposable {
        return if (!isNetworkAvailable()) {
            onError(NetworkErrorException("NO Inet"))
            emptyDisposable
        } else {
            subscribe(onSuccess, onError)
        }

    }


    fun <T> Observable<T>.subScribeWithNetwork(
            onSuccess: (T) -> Unit,
            onError: (Throwable) -> Unit = ERROR_CONSUMER
    ): Disposable {
        return if (!isNetworkAvailable()) {
            onError(NetworkErrorException("NO Inet"))
            emptyDisposable
        } else {
            subscribe(onSuccess, onError)
        }

    }

    fun <T> Observable<T>.safeSubScribeWithNetwork(
            onNext: (T) -> Unit,
            onError: (Throwable) -> Unit = ERROR_CONSUMER,
            onComplete: () -> Unit
    ): Disposable {
        return if (!isNetworkAvailable()) {
            subscribe(safeOnNext(onNext), safeOnError(onError), safeOnComplete(onComplete))
        } else {
            subscribe(onNext, onError, onComplete)
        }

    }
    fun <T> Observable<T>.safeSubScribeWithNetwork(
            onNext: (T) -> Unit,
            onError: (Throwable) -> Unit = ERROR_CONSUMER

    ): Disposable {
        return if (!isNetworkAvailable()) {
            subscribe(safeOnNext(onNext), safeOnError(onError), safeOnComplete({}))
        } else {
            subscribe(onNext, onError, {})
        }

    }
    fun <T> Single<T>.safeSubScribeWithNetwork(
            onSuccess: (T) -> Unit,
            onError: (Throwable) -> Unit = ERROR_CONSUMER
    ): Disposable {
        return if (!isNetworkAvailable()) {
            subscribe(safeOnNext(onSuccess), safeOnError(onError))
        } else {
            subscribe(onSuccess, onError)
        }

    }

    private val EMPTY_ACTION: () -> Unit = {}
    private fun safeOnComplete(onComplete: () -> Unit): () -> Unit {
        if (onComplete == EMPTY_ACTION) {
            return EMPTY_ACTION
        } else {
            return { execute(onComplete) }
        }
    }

    private fun <T> safeOnNext(onNext: (T) -> Unit): (T) -> Unit {
        return { nextItem -> execute { onNext(nextItem) } }
    }

    private fun safeOnError(onError: (Throwable) -> Unit): (Throwable) -> Unit {
        if (onError == ERROR_CONSUMER) {
            return ERROR_CONSUMER
        } else {
            return { error: Throwable -> execute { onError(error) } }
        }
    }


    private fun execute(action: () -> Unit) {
        pendingActions.add(action)
    }

    fun Disposable.addToDisposable() {
        addTo(compositeDisposable)
    }

    private fun isNetworkAvailable(): Boolean {
        return if (weekReference.get() == null) {
            false
        } else {
            val connectivityManager = weekReference.get()!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    }
}
