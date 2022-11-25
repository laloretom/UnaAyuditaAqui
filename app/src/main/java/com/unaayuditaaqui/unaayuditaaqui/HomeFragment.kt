package com.unaayuditaaqui.unaayuditaaqui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.unaayuditaaqui.unaayuditaaqui.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var serviceRecyclerView: RecyclerView
    private lateinit var serviceArrayList: ArrayList<Service>
    private lateinit var uid: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        serviceRecyclerView = view.findViewById(R.id.servicesList)
        serviceRecyclerView.layoutManager = LinearLayoutManager(context)
        serviceRecyclerView.setHasFixedSize(true)

        serviceArrayList = arrayListOf<Service>()
        getServiceData()
        //serviceArrayList.clear()

    }

    private fun getServiceData(){
        dbRef = FirebaseDatabase.getInstance().getReference("Services")
        val db: Query = dbRef.orderByChild("dateInverted")
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (serviceSnapshot in snapshot.children){
                        val service = serviceSnapshot.getValue(Service::class.java)
                        serviceArrayList.add(service!!)
                    }
                    val reversedView: MutableList<Service> = ArrayList(serviceArrayList.asReversed())
                    serviceRecyclerView.adapter = ServiceAdapter(reversedView)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}

