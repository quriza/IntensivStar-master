package ru.androidschool.intensiv.network


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers


fun <T> Single<T>.applySchedulers()= (
        this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()))
