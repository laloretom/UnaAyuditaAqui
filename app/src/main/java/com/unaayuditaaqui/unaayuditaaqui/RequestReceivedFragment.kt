package com.unaayuditaaqui.unaayuditaaqui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.unaayuditaaqui.unaayuditaaqui.databinding.FragmentRequestReceivedBinding


class RequestReceivedFragment : Fragment() {

    private var _binding: FragmentRequestReceivedBinding? = null
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
        _binding = FragmentRequestReceivedBinding.inflate(inflater, container, false)
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
        val myRef = FirebaseDatabase.getInstance().getReference("Users/$uid/Request/Received")
        val db: Query = myRef.orderByChild("state").equalTo("Pendiente")
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
        db.addValueEventListener(messagesListener)

        acctionRequest(recyclerView)
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
            holder.fromUser.text = currentItem.fromName

            holder.itemView.setOnClickListener { v ->
                val intent = Intent(v.context, DetailServiceActivity::class.java).apply {
                    putExtra("key",currentItem.idService )
                }
                v.context.startActivity(intent)
            }

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val serviceTitle : TextView = itemView.findViewById(R.id.titleTextView)
            val message: TextView = itemView.findViewById(R.id.messageTextView2)
            val state: TextView = itemView.findViewById(R.id.stateTextView2)
            val date: TextView = itemView.findViewById(R.id.dateTextView)
            val fromUser: TextView = itemView.findViewById(R.id.fromTextView2)
        }
    }

    private fun acctionRequest(recyclerView: RecyclerView){
        val db = FirebaseDatabase.getInstance().getReference("Users")
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when (direction){
                    ItemTouchHelper.LEFT -> {
                        requestMutableList[viewHolder.adapterPosition].idRequest?.let {

                            db.child(requestMutableList[viewHolder.adapterPosition].toUid.toString())
                                .child("Request").child("Received")
                                .child(it).child("state").setValue("Rechazada")

                            db.child(requestMutableList[viewHolder.adapterPosition].fromUid.toString())
                                .child("Request").child("Sent")
                                .child(it).child("state").setValue("Rechazada")
                        }
                        requestMutableList.removeAt(viewHolder.adapterPosition)
                    }
                    ItemTouchHelper.RIGHT -> {
                        requestMutableList[viewHolder.adapterPosition].idRequest?.let {

                            db.child(requestMutableList[viewHolder.adapterPosition].toUid.toString())
                                .child("Request").child("Received")
                                .child(it).child("state").setValue("Aceptada")

                            db.child(requestMutableList[viewHolder.adapterPosition].fromUid.toString())
                                .child("Request").child("Sent")
                                .child(it).child("state").setValue("Aceptada")
                        }
                    }
                }
                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}