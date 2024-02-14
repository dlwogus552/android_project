package org.third.medicalapp.user

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.third.medicalapp.R
import org.third.medicalapp.databinding.FragmentLikeHospitalBinding
import org.third.medicalapp.databinding.FragmentLikePharmacyBinding
import org.third.medicalapp.databinding.FragmentMyPageBinding
import org.third.medicalapp.util.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikePharmacyFragment : Fragment() {

    private var _binding: FragmentLikePharmacyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLikePharmacyBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }
    // on create 종료
    override fun onStart() {
        super.onStart()

        val activity = activity as UserMainActivity
        //회원정보 값 받아오기
        val sharedPref = activity.getSharedPreferences("User", AppCompatActivity.MODE_PRIVATE)
        sharedPref.getString("nickName", "-")


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}