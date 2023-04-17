package com.app.bookstoreapp.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.bookstoreapp.BuildConfig
import com.app.bookstoreapp.MainActivity
import com.app.bookstoreapp.R
import com.app.bookstoreapp.databinding.FragmentMenuScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class MenuScreenFragment : Fragment() {
    private val sharedPreference by lazy {
        requireActivity().getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}_sharePreferences",
            Context.MODE_PRIVATE
        )
    }
    private lateinit var binding: FragmentMenuScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        lister()
        initView()
    }

    private fun initView() {
        binding.apply {
            tvNameProfile.text = sharedPreference.getString("username", "name")
            Picasso.get().load(
                sharedPreference.getString(
                    "userImage",
                    "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png"
                )
            )
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .fit()
                .into(imgAvatar)
        }
    }

    private fun getData() {}

    private fun lister() {
        binding.apply {
            tvMyProfile.setOnClickListener {
                findNavController().navigate(R.id.profileScreenFragment)
            }
            tvLogout.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                startActivity(
                    Intent(
                        requireActivity(),
                        MainActivity::class.java
                    )
                )
                requireActivity().finish()
            }
        }
    }
}