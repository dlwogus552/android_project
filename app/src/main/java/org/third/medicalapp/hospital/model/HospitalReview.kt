package org.third.medicalapp.hospital.model

data class HospitalReview(
    var hospitalName : String? = null,
    var hospitalReviewId : String? = null,
    var email : String? = null,
    var review : String? = null,
    var date : String? = null,
    var isLiked : Boolean = false
)
