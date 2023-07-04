package com.app.bookstoreapp.screens

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.bookstoreapp.R
import com.app.bookstoreapp.databinding.FragmentBookDetailScreenBinding
import com.app.bookstoreapp.models.Book
import com.app.bookstoreapp.models.Cart
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class BookDetailScreenFragment : Fragment() {
    private val TAG = "BookDetailScreen"
    private lateinit var binding: FragmentBookDetailScreenBinding
    private lateinit var database: DatabaseReference
    private lateinit var book: Book

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookDetailScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        lister()
        postponeEnterTransition()
    }

    private fun lister() {
        binding.apply {
            icBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnAddToCart.setOnClickListener {
                confirmAddBook()
            }
        }
    }

    private fun initView() {
        var testId = ""
        arguments?.apply {
            testId = this.get("idBook").toString()
//            Toast.makeText(requireContext(), "Id product is :$testId", Toast.LENGTH_SHORT).show()
        }
        database = FirebaseDatabase.getInstance().getReference("Book").child(testId)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot != null) {
                    book = snapshot.getValue(Book::class.java)!!
                    Log.d(TAG, "onDataChange: $book")

                    binding.apply {
                        Picasso.get().load(book.imageUrl)
                            .placeholder(R.mipmap.ic_launcher)
                            .centerCrop()
                            .fit()
                            .into(imgDetailBook, object : Callback {
                                override fun onSuccess() {
                                    startPostponedEnterTransition()
                                }

                                override fun onError(e: Exception?) {
                                    startPostponedEnterTransition()
                                }
                            })

                        tvNameBook.text = book.name
                        tvTitleBook.text = book.title
                        tvDescribeBook.text = book.describe
                        tvPriceBook.text = "RS. ${book.price}"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun addToCart() {
        val user = Firebase.auth.currentUser
        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(
            user!!.uid
        ).child(book.id)
        database.setValue(Cart(book.id, book.name, 1, book.price, book.imageUrl))
    }

    //    val database: Task<Void> = FirebaseDatabase.getInstance().getReference("Cart").child(
//        user!!.uid
//    ).removeValue()
    private fun confirmAddBook() {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        addToCart()
                        findNavController().navigate(R.id.cartScreenFragment2)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        dialog.dismiss()
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show()
    }
}