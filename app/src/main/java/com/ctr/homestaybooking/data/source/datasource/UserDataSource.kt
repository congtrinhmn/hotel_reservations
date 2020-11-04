package com.ctr.homestaybooking.data.source.datasource

import com.ctr.homestaybooking.data.source.request.LoginBody
import com.ctr.homestaybooking.data.source.request.RegisterBody
import com.ctr.homestaybooking.data.source.response.LoginResponse
import com.ctr.homestaybooking.data.source.response.RegisterResponse
import com.ctr.homestaybooking.data.source.response.UserResponse
import io.reactivex.Single

/**
 * Created by at-trinhnguyen2 on 2020/06/17
 */
interface UserDataSource {
    fun login(body: LoginBody): Single<LoginResponse>

    fun register(body: RegisterBody): Single<RegisterResponse>

    fun getUserFollowId(userId: Int): Single<UserResponse>
}