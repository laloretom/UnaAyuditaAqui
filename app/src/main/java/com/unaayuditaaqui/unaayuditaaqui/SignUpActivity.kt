package com.unaayuditaaqui.unaayuditaaqui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivitySignUpBinding
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()

        binding.signUpButton.setOnClickListener {
            val muserName = binding.usernameEditText.text.toString()
            val mfirstName = binding.firstNameEditText.text.toString()
            val mlastName = binding.lastNameEditText.text.toString()
            val mEmail = binding.emailEditText.text.toString()
            val mPassword = binding.passwordEditText.text.toString()
            val mConfirmPassword = binding.confirmPasswordEditText.text.toString()

            val passwordRegex = Pattern.compile("^" +
                    "(?=.*[-@#$%^&+=])" +     // Al menos 1 carácter especial
                    ".{6,}" +                // Al menos 4 caracteres
                    "$")

            if (muserName.isEmpty()){
                Toast.makeText(this, "Campo obligatorio.",
                    Toast.LENGTH_SHORT).show()
            } else if (mfirstName.isEmpty()){
                Toast.makeText(this, "Campo obligatorio.",
                    Toast.LENGTH_SHORT).show()
            }else if (mlastName.isEmpty()){
                Toast.makeText(this, "Campo obligatorio.",
                    Toast.LENGTH_SHORT).show()
            }else if (mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                Toast.makeText(this, "Ingrese un email valido.",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword.isEmpty() || !passwordRegex.matcher(mPassword).matches()){
                Toast.makeText(this, "La contraseña es debil.",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword != mConfirmPassword){
                Toast.makeText(this, "Confirma la contraseña.",
                    Toast.LENGTH_SHORT).show()
            } else {
                createAccount(mEmail, mPassword, muserName, mfirstName, mlastName)
            }

        }

        binding.backImageView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            this.startActivity(intent)
        }

    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            if(currentUser.isEmailVerified){
                val intent = Intent(this, MainActivity::class.java)
                this.startActivity(intent)
            } else {
                val intent = Intent(this, CheckEmailActivity::class.java)
                this.startActivity(intent)
            }
        }
    }

    private fun createAccount(
        email: String,
        password: String,
        userName: String,
        firstName: String,
        lastName: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    val profileUpdates = userProfileChangeRequest {
                        displayName = userName
                    }

                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                val databaseRef = database.reference.child("Users").child(auth.currentUser!!.uid)
                                val user : User = User(auth.currentUser!!.uid,userName,firstName,lastName,email)
                                databaseRef.setValue(user).addOnCompleteListener {
                                    if(it.isSuccessful){
                                        val intent = Intent(this, CheckEmailActivity::class.java)
                                        this.startActivity(intent)
                                    } else {
                                        Toast.makeText(this, "No se pudo guardar los datos",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }

                            } else {
                                Toast.makeText(this, "No se pudo cambiar cambiar el Nombre de Usuario",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }

                } else {
                    Toast.makeText(this, "No se pudo crear la cuenta. Vuelva a intertarlo",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}