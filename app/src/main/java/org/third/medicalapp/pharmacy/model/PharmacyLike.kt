package org.third.medicalapp.pharmacy.model

data class PharmacyLike(
    var likeId : String?= null,
    var like_pharmacyName : String? = null,
    var like_email : String? = null,
    var isLiked : Boolean = false
)
