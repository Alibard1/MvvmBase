package tm.alibard.mvvm

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


abstract class BaseViewModel<N> : ViewModel() {
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
}
