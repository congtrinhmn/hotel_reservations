package com.ctr.homestaybooking.ui.sheme

import com.ctr.homestaybooking.data.source.datasource.LocalDataSource

/**
 * Created by at-phutran4 on 25/09/2020.
 */
class SchemeViewModel(private val localRepository: LocalDataSource) : SchemeVMContract {

    override fun getToken(): String? = localRepository.getAutoLoginToken()


}
