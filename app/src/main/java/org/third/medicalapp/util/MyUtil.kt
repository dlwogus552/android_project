package org.third.medicalapp.util

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Objects

fun myCheckPermission(activity: AppCompatActivity) {
    val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(activity, "권한 승인", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "권한 거부", Toast.LENGTH_SHORT).show()
        }
    }

    if (ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) !== PackageManager.PERMISSION_GRANTED
    ) {
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    if(ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.CALL_PHONE
        ) !== PackageManager.PERMISSION_GRANTED
    ){
        requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
    }
}

fun dateToString(date: Date?): String {
    val format = SimpleDateFormat("yyyy-MM-dd")
    return format.format(date)
}


//server에서 값 받아오기
class Result(
    result:Objects
)
