package com.app.bookstoreapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.bookstoreapp.R
import com.app.bookstoreapp.databinding.ItemNewBookBinding
import com.app.bookstoreapp.models.Book
import com.squareup.picasso.Picasso

class SearchBookAdapter(private val context: Context, private var mList: List<Book>, private var onItemClicked: ((book: Book) -> Unit)) :
    RecyclerView.Adapter<SearchBookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemNewBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemBook: Book = mList[position]
        holder.setView(itemBook)
    }
    fun filterList(filterList:ArrayList<Book>){
        mList = filterList
        notifyDataSetChanged()

    }
    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(private val itemBinding: ItemNewBookBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun setView(itemBook: Book) {
            itemBinding.apply {
                Picasso.get().load(itemBook.imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .centerCrop()
                    .fit()
                    .into(imgItemNewBie)
                tvNameBook.text = itemBook.name
                tvTitleBook.text = itemBook.title
                tvPriceBook.text = "RS. ${itemBook.price}"
                root.setOnClickListener {
                    onItemClicked(itemBook)
                }
            }
        }
    }
}
