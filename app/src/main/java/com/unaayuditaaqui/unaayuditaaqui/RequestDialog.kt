package com.unaayuditaaqui.unaayuditaaqui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.unaayuditaaqui.unaayuditaaqui.databinding.RequestInputBinding

class RequestDialog (
    private val onSubmitClickListener: (String) -> Unit
): DialogFragment() {
    private lateinit var binding : RequestInputBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = RequestInputBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)


        binding.requestButton.setOnClickListener {
            onSubmitClickListener.invoke(binding.messageEditText.text.toString())
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }


}