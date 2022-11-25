package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.unaayuditaaqui.unaayuditaaqui.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  Inflar el diseño y vincularlo a _binding
        _binding = FragmentDashboardBinding.inflate(inflater,container,false)

        binding.navServices.setOnClickListener{
            val intent = Intent(activity, MyServicesActivity::class.java)
            this.startActivity(intent)
        }

        binding.navRequestReceived.setOnClickListener {
            val intent = Intent(activity, RequestReceivedActivity::class.java)
            this.startActivity(intent)
        }

        binding.addService.setOnClickListener {
            val intent = Intent(activity, AddServiceActivity::class.java)
            this.startActivity(intent)
        }

        binding.navRequestSent.setOnClickListener {
            val intent = Intent(activity, RequestSentActivity::class.java)
            this.startActivity(intent)
        }

        //  Inflar el diseño de este fragmento
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}