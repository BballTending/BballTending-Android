package com.bballtending.android.data.repository

import com.bballtending.android.domain.login.model.RequestLogin
import com.bballtending.android.domain.login.model.ResponseLogin
import com.bballtending.android.domain.login.repository.LoginRepository
import com.bballtending.android.domain.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val networkRepository: NetworkRepository
) : LoginRepository {
    override suspend fun requestLogin(pRequestLogin: RequestLogin): NetworkResult<ResponseLogin> {
        return withContext(Dispatchers.IO) {
            delay(2000)
            NetworkResult.Fail("아이디와 비밀번호를 확인해 주세요.")
        }
    }

    companion object {
        private const val TAG: String = "LoginRepositoryImpl"
    }
}