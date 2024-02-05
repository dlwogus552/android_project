package org.third.medicalapp.sign.util

import org.third.medicalapp.sign.model.UserModel
import org.third.medicalapp.sign.model.UserModelList
import org.third.medicalapp.util.Result
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface INetworkService {
    @GET("check/{nickName}")
    fun checkNick(@Path("nickName") nickName: String): Call<Boolean>
    @GET("checkUser/{userName}")
    fun checkUser(@Path("userName") userName: String) : Call<UserModel>
    @GET("list")
    fun getUserList():Call<UserModelList>
    @PUT("modify")
    fun modify(@Body userModel: UserModel):Call<Result>

    @POST("insert")
    fun insert(@Body userModel: UserModel):Call<Result>
}