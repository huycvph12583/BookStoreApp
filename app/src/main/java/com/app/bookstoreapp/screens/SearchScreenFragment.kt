package com.app.bookstoreapp.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.app.bookstoreapp.BuildConfig
import com.app.bookstoreapp.R
import com.app.bookstoreapp.adapters.SearchBookAdapter
import com.app.bookstoreapp.databinding.FragmentSearchScreenBinding
import com.app.bookstoreapp.models.Book
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class SearchScreenFragment : Fragment() {
    private val sharedPreference by lazy {
        requireActivity().getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}_sharePreferences",
            Context.MODE_PRIVATE
        )
    }
    private var listBook = ArrayList<Book>()
    private lateinit var database: DatabaseReference
    private lateinit var adapter: SearchBookAdapter
    private lateinit var binding: FragmentSearchScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        binding.apply {

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
            adapter = SearchBookAdapter(requireContext(), listBook) {}
            rycSearch.adapter = adapter
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    filter(p0.toString())
                    return false
                }
            })
        }
    }

    private fun getData() {
        binding.apply {
            database = FirebaseDatabase.getInstance().getReference("Book")
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (listBook.size > 0) {
                        listBook = arrayListOf()
                    }
                    for (postSnapshot in snapshot.children) {
                        val item = postSnapshot.getValue(Book::class.java)
                        if (item != null) {
                            listBook.add(item)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun filter(text: String) {
        val filteredList = ArrayList<Book>()
        for (item in listBook) {
            if (item.name.lowercase().contains(text.lowercase())) {
                filteredList.add(item)
            }
        }
        if (filteredList.size == 0) {
            adapter.filterList(filteredList)
        } else {
            adapter.filterList(filteredList)
        }
    }
}
