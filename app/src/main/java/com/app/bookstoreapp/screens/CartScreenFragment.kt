package com.app.bookstoreapp.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.bookstoreapp.adapters.CartAdapter
import com.app.bookstoreapp.databinding.FragmentCartScreenBinding
import com.app.bookstoreapp.models.Bill
import com.app.bookstoreapp.models.BillDetail
import com.app.bookstoreapp.models.Cart
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class CartScreenFragment : Fragment() {
    val TAG = "CartScreen"
    var sumQuantity = 0
    var sumMoney = 0
    private lateinit var binding: FragmentCartScreenBinding
    private lateinit var database: DatabaseReference
    private lateinit var cartAdapter: CartAdapter
    private var listCart: MutableList<Cart> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            getData()
            lister()
        }
    }

    private fun lister() {
        binding.apply {
            btnBuy.setOnClickListener {
                val idBill: String = database.push().key ?: "1"
                database = FirebaseDatabase.getInstance().getReference("Bill")
                database.child(idBill).setValue(
                    Bill(
                         Firebase.auth.uid!!,idBill, sumQuantity,sumMoney,
                        getDate()
                    )
                )
                Bill()
                database = FirebaseDatabase.getInstance().getReference("BillDetail")
                for (item in listCart) {
                    database.push().setValue(
                        BillDetail(
                            idBill, item.idFood, item.name, item.quantity, item.price, item
                                .imageUrl
                        )
                    )
                }
                FirebaseDatabase.getInstance().getReference("Cart").removeValue()
            }

            icBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun getData() {
        binding.apply {
            val user = Firebase.auth.currentUser
            database = FirebaseDatabase.getInstance().getReference("Cart").child(user!!.uid)
            database.addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (listCart.size > 0) {
                        sumMoney = 0
                        sumQuantity = 0
                        listCart = mutableListOf()
                    }
                    for (postSnapshot in snapshot.children) {
                        val item = postSnapshot.getValue(Cart::class.java)
                        if (item != null) {
                            listCart.add(item)
                            sumQuantity += item.quantity
                            sumMoney += (item.quantity * item.price)
                        }
                    }
                    creatView()
                    tvSumPrice.text = "Sum Price:${sumMoney}$"
                    tvSumQuantity.text = "Quantity:${sumQuantity}"

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun creatView() {
        cartAdapter = CartAdapter(
            Firebase.auth.currentUser!!.uid,
            listCart
        ) { _, _, _ -> }
        binding.rycCart.adapter = cartAdapter
        cartAdapter.notifyDataSetChanged()
    }

    private fun getDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        return sdf.format(Date())
    }
}