package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.unaayuditaaqui.unaayuditaaqui.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    /*  Asignar la variable _binding inicialmente a nulo y
    /   También cuando la vista se destruye nuevamente, debe establecerse en nulo
    */
    private var _binding: FragmentAccountBinding? = null

    /*  Con la propiedad de respaldo del kotlin que extraemos
    /   el valor no nulo de _binding
    */
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  Inflar el diseño y vincularlo a _binding
        _binding = FragmentAccountBinding.inflate(inflater,container,false)

        updateUI ()

        binding.editInfoImage.setOnClickListener{
            val intent = Intent(activity, EditProfileActivity::class.java)
            this.startActivity(intent)
        }

        binding.rootRemoveAds.setOnClickListener{
            Toast.makeText(activity,"Eliminar ADS", Toast.LENGTH_SHORT).show()
        }

        binding.rootHomePage.setOnClickListener {
            val intent = Intent(activity, MyAccountActivity::class.java)
            this.startActivity(intent)
        }

        binding.rootMessages.setOnClickListener {
            Toast.makeText(activity,"Mis mensajes", Toast.LENGTH_SHORT).show()
        }

        binding.rootRegion.setOnClickListener {
            Toast.makeText(activity,"Cambiar región", Toast.LENGTH_SHORT).show()
        }

        binding.rootSetting.setOnClickListener {
            Toast.makeText(activity,"Configuración", Toast.LENGTH_SHORT).show()
        }

        binding.rootShare.setOnClickListener {
            Toast.makeText(activity,"Compartir", Toast.LENGTH_SHORT).show()
        }

        binding.rootHelp.setOnClickListener {
            Toast.makeText(activity,"Ayuda", Toast.LENGTH_SHORT).show()
        }

        binding.rootAbout.setOnClickListener {
            Toast.makeText(activity,"Acerca de", Toast.LENGTH_SHORT).show()
        }

        binding.rootLogOut.setOnClickListener {
            signOut();
        }

        //  Inflar el diseño de este fragmento
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private  fun updateUI () {
        val user = auth.currentUser

        if (user != null){
            binding.emailTextView.text = user.email

            if(user.displayName != null){
                binding.userName.text = user.displayName
                binding.userName.setText(user.displayName)
            }

            Glide
                .with(this)
                .load(user.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_profile)
                .into(binding.headerImage)
        }
    }

    private  fun signOut(){
        Firebase.auth.signOut()
        val intent = Intent(activity, SignInActivity::class.java)
        this.startActivity(intent)
    }


}