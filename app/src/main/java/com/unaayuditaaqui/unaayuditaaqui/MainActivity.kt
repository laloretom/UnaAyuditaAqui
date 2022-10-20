package com.unaayuditaaqui.unaayuditaaqui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    //  Crea una instancia de enlace para la actividad_principal.xml
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()

        replaceFragment(DashboardFragment())

        binding.bottomBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.tab0 -> replaceFragment(DashboardFragment())
                R.id.tab1 -> replaceFragment(HomeFragment())
                R.id.tab2 -> replaceFragment(SearchFragment())
                R.id.tab3 -> replaceFragment(AccountFragment())
                else -> {}
            }
            true


        }
    }

    //  Función para cambiar el fragmento que se usa para reducir las líneas de código
    private  fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_layout,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}