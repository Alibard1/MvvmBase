package tm.alibard.mvvmbase

import android.content.Context
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import tm.alibard.mvvm.BaseViewModel
import java.util.*


class TestVM ( context:Context) :BaseViewModel<Navigator>(context){
    fun toDo(){
        val testList = arrayListOf<Int>(1,2,3,4,5,6)
        Observable.fromIterable(testList)
                .safeSubScribeWithNetwork({
                    Log.d("TAG",it.toString())
                },{
                    Log.d("TAG","",it)
                }).addToDisposable()
    }

}