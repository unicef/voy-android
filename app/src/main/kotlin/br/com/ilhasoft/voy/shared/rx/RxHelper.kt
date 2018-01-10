package br.com.ilhasoft.voy.shared.rx

import io.reactivex.FlowableTransformer
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by lucasbarros on 09/01/18.
 */
object RxHelper {

    fun <Type> defaultSingleSchedulers() = SingleTransformer<Type, Type> {
        it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun <Type> defaultFlowableSchedulers() = FlowableTransformer<Type, Type> {
        it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}