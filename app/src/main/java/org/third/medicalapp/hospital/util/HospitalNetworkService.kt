package org.third.medicalapp.hospital.util

import org.third.medicalapp.hospital.model.Hospital
import org.third.medicalapp.hospital.model.HospitalList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HospitalNetworkService {
    @GET("home")
    fun doHome(): Call<String>
    @GET("list")
    fun doGetHospitalList(): Call<HospitalList>
    @GET("getHospitalId/{id}")
    fun doGetHospitalId(@Path("h_id") id: Long): Call<Hospital>
    @GET("getUsername/{name}")
    fun doGetHospitalname(@Path("h_name") name: String): Call<Hospital>
    @POST("insert")
    fun insert(@Body hospital: Hospital): Call<String>
}