package org.third.medicalapp.hospital.model

data class HospitalLike(
    var likeId : String?= null,
    var like_pharmacyId : Long? = null,
    var like_email : String? = null,
    var isLiked : Boolean = false
)
