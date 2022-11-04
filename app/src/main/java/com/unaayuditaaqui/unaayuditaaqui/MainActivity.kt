package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
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
    private lateinit var builder: AlertDialog.Builder
    //private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()

        //replaceFragment(HomeFragment())

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

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null){
            sendToSignInActivity()
        }else{
            replaceFragment(HomeFragment())
        }
    }

    //  Función para cambiar el fragmento que se usa para reducir las líneas de código
    private  fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_layout,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    /*
    override fun onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
        }else{
            Toast.makeText(applicationContext, "Dos veces para salir", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

     */


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            val alertDialog : AlertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("Salir")
            alertDialog.setMessage("Desea salir de UnaAyuditaAqui")

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Si"){
                dialog, which -> finish()
                dialog.dismiss()}

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"No"){
                dialog, which ->
                dialog.dismiss()}
            replaceFragment(HomeFragment())
            alertDialog.show()
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun sendToSignInActivity(){
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

}