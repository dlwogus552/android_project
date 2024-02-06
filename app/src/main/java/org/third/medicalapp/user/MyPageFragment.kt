package org.third.medicalapp.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.third.medicalapp.databinding.FragmentMyPageBinding
import org.third.medicalapp.sign.model.UserModel
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.MyApplication.Companion.auth
import org.third.medicalapp.util.MyApplication.Companion.email
import org.third.medicalapp.util.dateToString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyPageFragment : Fragment() {

    private var _binding: FragmentMyPageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val activity=activity as UserMainActivity
        Log.d("aaaa","Create View")
        //버튼 리스너
        //로그아웃 버튼
        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            email = null
            activity.finish()
        }
        //수정버튼
        binding.modifyBtn.setOnClickListener{
            startActivity(Intent(context,ModifyInfoActivity::class.java))
        }


        //값 받아오기
        val networkService =
            (activity.applicationContext as MyApplication).netWorkService
        val check = networkService.checkUser(email.toString())
        var userModel: UserModel? =null
        check.enqueue(object : Callback<UserModel>{
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                Log.d("aaa","${response.body()?.userName}")
                if(response.body()!=null){
                    userModel=response.body()
                    binding.userNameText.text=userModel?.userName
                    binding.nickNameText.text=userModel?.nickName
                    if(userModel?.phoneNumber!=null){
                        binding.phoneNumberText.text=userModel?.phoneNumber
                    }else{
                        binding.phoneNumberText.text="없음"
                    }
                    binding.regiDateText.text= userModel?.regiDate?.substring(0,10)

                }else{
                    Log.d("aaa","error")
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.d("aaaa","실패")
                call.cancel()
            }

        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}