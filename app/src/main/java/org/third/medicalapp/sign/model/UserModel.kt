package org.third.medicalapp.sign.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import java.util.Objects

class UserModel(
    var userName:String,
    var nickName:String,
    var phoneNumber:String?,
    var regiDate:String?,
    var roles:String?
)
