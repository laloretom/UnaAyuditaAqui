package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unaayuditaaqui.unaayuditaaqui.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
    /*  Asignar la variable _binding inicialmente a nulo y
    /   También cuando la vista se destruye nuevamente, debe establecerse en nulo
    */

    private var _binding: FragmentDashboardBinding? = null

    /*  Con la propiedad de respaldo del kotlin que extraemos
    /   el valor no nulo de _binding
    */
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

        //  Inflar el diseño de este fragmento
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}