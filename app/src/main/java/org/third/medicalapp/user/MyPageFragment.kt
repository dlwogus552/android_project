package org.third.medicalapp.user

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.Task
import org.third.medicalapp.R
import org.third.medicalapp.databinding.FragmentMyPageBinding
import org.third.medicalapp.sign.model.UserModel
import org.third.medicalapp.util.MyApplication
import org.third.medicalapp.util.MyApplication.Companion.auth
import org.third.medicalapp.util.MyApplication.Companion.email
import org.third.medicalapp.util.MyApplication.Companion.storage
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
        setHasOptionsMenu(true)
        return root
    }
    // on create 종료
    override fun onStart() {
        super.onStart()
        //딜레이
        Handler(Looper.getMainLooper()).postDelayed({
            val activity=activity as UserMainActivity
            //회원정보 값 받아오기
            val networkService =
                (activity.applicationContext as MyApplication).netWorkService
            val check = networkService.checkUser(email.toString())
            var userModel: UserModel?
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
                    Log.d("aaaa","info 서버연결 실패")
                    call.cancel()
                }
            })
            // networtService 종료
            // profile 지정
            val storageRef = storage.reference.child("images/profile/${email}.jpg")
            // 파일 존재 여부 확인
            Log.d("aaaa","파일존재여부 확인 전")
            storageRef.metadata.addOnSuccessListener { metadata ->
                Log.d("aaaa","파일존재여부 확인 중")
                storageRef.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Glide.with(requireContext())
                            .load(task.result)
                            .into(binding.profilePicture)
                    }
                }
            }.addOnFailureListener {exception->
                Log.d("aaaa","파일존재여부 확인 중")
                binding.profilePicture.setImageResource(R.drawable.basic_profile)
            }
        }, 1500) // 1000ms = 1초
    }


    // 메뉴
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_my_page, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_modify -> {
                startActivity(Intent(context,ModifyInfoActivity::class.java))
                return true
            }

            R.id.menu_logout -> {
                auth.signOut()
                email = null
                (activity as UserMainActivity).finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}