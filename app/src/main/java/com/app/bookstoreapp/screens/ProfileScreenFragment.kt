package com.app.bookstoreapp.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.bookstoreapp.BuildConfig
import com.app.bookstoreapp.R
import com.app.bookstoreapp.databinding.FragmentProfileScreenBinding
import com.app.bookstoreapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ProfileScreenFragment : Fragment() {
    private lateinit var binding: FragmentProfileScreenBinding
    private lateinit var database: DatabaseReference
    private lateinit var user: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        getData()
        lister()
    }


    private fun getData() {
        database = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().uid.toString())
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                if (user != null) {
                    initView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        )
    }
    private fun lister(){
        binding.apply {
            icBack.setOnClickListener {
                findNavController().navigate(R.id.menuScreenFragment)
            }
        }
    }
    private fun initView() {
        binding.apply {
            var link =
                "https://t3.ftcdn.net/jpg/03/46/83/96/360_F_346839683_6nAPzbhpSkIpb8pmAwufkC7c5eD7wYws.jpg"
            if (!user.linkImage.isNullOrEmpty()) {
                link = user.linkImage
            }

            Picasso.get().load(link)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .fit()
                .into(imgProfile, object : Callback {
                    override fun onSuccess() {
                        startPostponedEnterTransition()
                    }

                    override fun onError(e: Exception?) {
                        TODO("Not yet implemented")
                    }
                })
            tvMyName.text = "Name :${user.username}"
            tvEmailProfile.setText("Email: ${user.email}")
            tvAddressProfile.setText("Address: ${user.addressUser}")
            tvDateProfile.setText("Date: ${user.dateofbirth}")
        }

    }
}