package com.gorkem.news.base

import com.google.gson.Gson
import com.gorkem.news.data.model.Response
import com.gorkem.news.data.model.ServiceResult
import retrofit2.HttpException


open class BaseRepository {
    suspend fun <T> call(networkCall: suspend () -> T): ServiceResult<T> {
        return try {
            val response: T = networkCall.invoke() ?: return ServiceResult.error<T>()

            var errorMessage: String? = null
            when (response) {
                is Response -> {
                    if (response.status.equals("error"))
                        errorMessage = response.message
                }
            }
            return if (errorMessage != null)
                ServiceResult.error(errorMessage, response)
            else
                ServiceResult.success(response)

        } catch (e: HttpException) {
            val errorBody = e.response()!!.errorBody()!!.string()
            val errorResponse = Gson().fromJson(errorBody, Response::class.java)
            ServiceResult.error(errorResponse.message)
        } catch (e: Exception) {
            ServiceResult.error() //Butun error buraya dusuyor
        }
    }
}