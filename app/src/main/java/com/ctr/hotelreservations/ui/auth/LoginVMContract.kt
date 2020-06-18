package com.ctr.hotelreservations.ui.auth

import com.ctr.hotelreservations.data.source.request.LoginBody
import com.ctr.hotelreservations.data.source.response.LoginResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
interface LoginVMContract {

    fun login(loginBody: LoginBody): Single<LoginResponse>

    fun getProgressObservable(): BehaviorSubject<Boolean>

    fun saveAutoLoginToken(token: String?)
}