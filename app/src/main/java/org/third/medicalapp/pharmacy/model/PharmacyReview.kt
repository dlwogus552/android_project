package org.third.medicalapp.pharmacy.model

data class PharmacyReview(
    var pharmacyName : String? = null,
    var pharmacyReviewId : String? = null,
    var email : String? = null,
    var review : String? = null,
    var date : String? = null,
    var isLiked : Boolean = false
)
