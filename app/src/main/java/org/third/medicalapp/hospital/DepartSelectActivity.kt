package org.third.medicalapp.hospital

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.third.medicalapp.R
import org.third.medicalapp.databinding.ActivityDepartSelectBinding

class DepartSelectActivity : AppCompatActivity() {
    lateinit var binding:ActivityDepartSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepartSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCloseX.setOnClickListener {
            finish()
        }

        binding.btn00.setOnClickListener { //모두

        }
        binding.btn01.setOnClickListener {//외과

        }
        binding.btn10.setOnClickListener {//성형외과

        }
        binding.btn11.setOnClickListener {//내과

        }
        binding.btn20.setOnClickListener {//산부인과

        }
        binding.btn21.setOnClickListener {//이비인후과

        }
        binding.btn30.setOnClickListener {//비뇨기과

        }
        binding.btn31.setOnClickListener {//소아과

        }
        binding.btn40.setOnClickListener {//정신과

        }
        binding.btn41.setOnClickListener {//안과

        }
        binding.btn50.setOnClickListener {//통증의학과

        }
        binding.btn51.setOnClickListener {//정형외과

        }
        binding.btn60.setOnClickListener {//치과

        }
        binding.btn61.setOnClickListener {//가정의학과

        }
        binding.btn70.setOnClickListener {//건강의학과

        }
        binding.btn71.setOnClickListener {//재활의학과

        }
        binding.btn80.setOnClickListener {//병리과

        }
        binding.btn81.setOnClickListener {//신경외과

        }

    }
}