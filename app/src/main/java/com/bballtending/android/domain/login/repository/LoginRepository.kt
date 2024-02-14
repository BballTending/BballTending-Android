package com.bballtending.android.domain.login.repository

import com.bballtending.android.domain.login.model.RequestLogin
import com.bballtending.android.domain.login.model.ResponseLogin
import com.bballtending.android.domain.network.NetworkResult

interface LoginRepository {
    suspend fun requestLogin(pRequestLogin: RequestLogin): NetworkResult<ResponseLogin>
}