package com.unaayuditaaqui.unaayuditaaqui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.unaayuditaaqui.unaayuditaaqui.databinding.ActivityDetailServiceBinding

class DetailServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailServiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val key = intent.getStringExtra("key")
        val key2 = getIntent().getStringExtra("key")

        binding.detalleTextView.setText(key)


    }
}