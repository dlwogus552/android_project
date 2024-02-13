package org.third.medicalapp.community

data class LikeData(
    var likeId : String?= null,
    var like_docId : String? = null,
    var like_email : String? = null,
    var isLiked : Boolean = false
)
//{
//    // isLiked 필드에 대한 setter 메소드
//    fun setIsLiked(liked: Boolean) {
//        this.isLiked = liked
//    }
//
//    // isLiked 필드에 대한 getter 메소드
//    fun getIsLiked(): Boolean {
//        return isLiked
//    }
//}
