package com.unaayuditaaqui.unaayuditaaqui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.unaayuditaaqui.unaayuditaaqui.databinding.FragmentRequestSentHistoryBinding


class RequestSentHistoryFragment : Fragment() {

    private var _binding: FragmentRequestSentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var requestRecyclerView: RecyclerView
    private val requestMutableList:MutableList<Request> = ArrayList()
    private lateinit var messagesListener: ValueEventListener
    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRequestSentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestRecyclerView = view.findViewById(R.id.requestRecyclerView)
        requestRecyclerView.layoutManager = LinearLayoutManager(context)
        requestRecyclerView.setHasFixedSize(true)

        setupRecyclerView(binding.requestRecyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val myRef = FirebaseDatabase.getInstance().getReference("Users/$uid/Request/Sent")
        messagesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                requestMutableList.clear()
                if (snapshot.exists()){
                    for (serviceSnapshot in snapshot.children){
                        val request = serviceSnapshot.getValue(Request::class.java)
                        requestMutableList.add(request!!)
                    }
                    val reversedView: MutableList<Request> = ArrayList(requestMutableList.asReversed())
                    recyclerView.adapter = RequestViewAdapter(reversedView)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }
        myRef.addValueEventListener(messagesListener)

    }

    class RequestViewAdapter(private val values: List<Request>) :
        RecyclerView.Adapter<RequestViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.request_content, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val currentItem = values[position]
            holder.serviceTitle.text = currentItem.serviceTitle
            holder.message.text = currentItem.message
            holder.state.text = currentItem.state
            holder.date.text = currentItem.date
            holder.toText.text = "Para:"
            holder.toUser.text = currentItem.toName

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val serviceTitle : TextView = itemView.findViewById(R.id.titleTextView)
            val message: TextView = itemView.findViewById(R.id.messageTextView2)
            val state: TextView = itemView.findViewById(R.id.stateTextView2)
            val date: TextView = itemView.findViewById(R.id.dateTextView)
            val toText: TextView = itemView.findViewById(R.id.fromTextView)
            val toUser: TextView = itemView.findViewById(R.id.fromTextView2)
        }
    }

}