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
    @GET("api/hospital/list")
    fun doGetHospitalList(): Call<HospitalList>
    @GET("api/hospital/byId/{id}")
    fun doGetHospitalId(@Path("id") id: Long): Call<Hospital>
    @GET("api/hospital/getUsername/{name}")
    fun doGetHospitalName(@Path("hname") name: String): Call<Hospital>
    @POST("api/hospital/insert")
    fun insert(@Body hospital: Hospital): Call<String>
}