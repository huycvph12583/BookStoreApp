package com.app.bookstoreapp.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.bookstoreapp.R
import com.app.bookstoreapp.adapters.BillHistoryBuyBookAdapter
import com.app.bookstoreapp.databinding.FragmentBillScreenBinding
import com.app.bookstoreapp.models.Bill
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BillScreenFragment : Fragment() {
    val TAG = "BillScreen"
    private lateinit var binding: FragmentBillScreenBinding
    private lateinit var database: DatabaseReference
    private lateinit var adapter: BillHistoryBuyBookAdapter
    private var listBill: MutableList<Bill> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        initView()
    }

    private fun getData() {
        binding.apply {
            database = FirebaseDatabase.getInstance().getReference("Bill")
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (listBill.size > 0) {
                        listBill = mutableListOf()
                    }
                    for (postSnapshot in snapshot.children) {
                        var item = postSnapshot.getValue(Bill::class.java)
                        if (item != null && item.idUser == FirebaseAuth.getInstance().uid!!) {
                            listBill.add(item)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }
    }

    private fun initView() {
        binding.apply {
            // List For You
            adapter = BillHistoryBuyBookAdapter(requireContext(), listBill) { bill ->


                var bundle = Bundle()
                bundle.putString("idBill", bill.idBill)
                findNavController().navigate(
                    R.id.action_billScreenFragment_to_billDetailScreenFragment,
                    bundle
                )
            }
            rycListBuySuccess.adapter = adapter
        }
    }
}