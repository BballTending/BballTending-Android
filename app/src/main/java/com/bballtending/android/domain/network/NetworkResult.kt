package com.bballtending.android.domain.network

sealed class NetworkResult<out R> {
    /**
     * 네트워크 통신 성공
     */
    data class Success<out T>(val data: T) : NetworkResult<T>()

    /**
     * 네트워크 통신 실패
     */
    data class Fail(val msg: String) : NetworkResult<Nothing>()

    /**
     * 네트워크 통신 에러
     */
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
}
