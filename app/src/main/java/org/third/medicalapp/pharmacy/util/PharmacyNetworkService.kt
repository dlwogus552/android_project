package org.third.medicalapp.pharmacy.util

import org.third.medicalapp.hospital.model.Hospital
import org.third.medicalapp.hospital.model.HospitalList
import org.third.medicalapp.pharmacy.model.Pharmacy
import org.third.medicalapp.pharmacy.model.PharmacyList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PharmacyNetworkService {

    @GET("api/pharmacy/list")
    fun doGetPharmacyList(): Call<PharmacyList>
    @GET("api/pharmacy/byId/{id}")
    fun doGetPharmacyId(@Path("id") id: Long): Call<Pharmacy>
    @GET("api/pharmacy/byPhar/{Pharmacy}")
    fun doGetPharmacyName(@Path("pharmacy") name: String): Call<Hospital>
    @POST("api/pharmacy/insert")
    fun insert(@Body hospital: Hospital): Call<String>
}