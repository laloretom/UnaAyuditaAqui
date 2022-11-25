package com.unaayuditaaqui.unaayuditaaqui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityRequestSentBinding

class RequestSentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRequestSentBinding
    private var tabTitle = arrayOf("Pendientes","Historial")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRequestSentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pager = binding.viewPager2
        val tb = binding.tabLayout
        pager.adapter = fragmentAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tb,pager){
                tab, position ->
            tab.text = tabTitle[position]
        }.attach()

    }

    class fragmentAdapter(fragmentManager: FragmentManager, lifecyle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecyle){

        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            when(position){
                0 -> return RequestSentFragment()
                1 -> return RequestSentHistoryFragment()
                else -> return RequestSentFragment()
            }
        }
    }
}