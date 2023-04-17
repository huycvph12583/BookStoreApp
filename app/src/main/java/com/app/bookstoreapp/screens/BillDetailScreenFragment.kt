package com.app.bookstoreapp.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.bookstoreapp.adapters.BillDetailAdapter
import com.app.bookstoreapp.databinding.FragmentBillDetailScreenBinding
import com.app.bookstoreapp.models.Bill
import com.app.bookstoreapp.models.BillDetail
import com.google.firebase.database.*

class BillDetailScreenFragment : Fragment() {
    private lateinit var databaseFirebase: DatabaseReference
    private lateinit var billDetail: Bill
    private lateinit var idBill: String
    private var listBillDetail: MutableList<BillDetail> = mutableListOf()
    private lateinit var adapter: BillDetailAdapter
    private lateinit var binding: FragmentBillDetailScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillDetailScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idBill = requireArguments().getString("idBill", "")
        getData()
    }

    private fun getData() {
        databaseFirebase = FirebaseDatabase.getInstance().getReference("Bill")
            .child(requireArguments().getString("idBill", ""))
        databaseFirebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                billDetail = snapshot.getValue(Bill::class.java)!!
                if (billDetail != null) {

                    initView(billDetail)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                val TAG = "Hello"
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

   FirebaseDatabase.getInstance().getReference("BillDetail").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (listBillDetail.size > 0) {
                    listBillDetail = mutableListOf()
                }
                for (postSnapshot in snapshot.children) {
                    val item = postSnapshot.getValue(BillDetail::class.java)
                    Log.d("hihi", "onDataChange2: "+listBillDetail.size+item!!.idBill+"Bill"+item)
                   if (item?.idBill.equals(idBill) ){
                        listBillDetail.add(item!!)
                 }

                }
                Log.d("hihi", "onDataChange1: "+listBillDetail.size+idBill)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                val TAG = "Hello"
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }

    private fun initView(bill: Bill) {
        if (bill != null) {
            Toast.makeText(requireContext(), "Bill not null:${bill}", Toast.LENGTH_SHORT)
                .show()
        }
        binding.apply {
            tvAddressBill.text = "Address: ${bill.address}"
            tvIdBill.text = "Code Bill : ${bill.idBill}"
            tvPriceBill.text = "Price: ${bill.sumPrice} $"
            tvDateBillDetail.text = "Order was purchased on ${bill.date}"

            if (listBillDetail != null) {
                adapter = BillDetailAdapter(requireContext(), listBillDetail) {}
                rycProductDetail.adapter = adapter
                adapter.notifyDataSetChanged()
            }

        }

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}