package com.app.bookstoreapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.bookstoreapp.R
import com.app.bookstoreapp.models.Book
import com.squareup.picasso.Picasso

class ForYouAdapter(private val context: Context, private val mList: List<Book>) :
    RecyclerView.Adapter<ForYouAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recyclerview_for_you, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemBook: Book = mList[position]
        Picasso.get().load(itemBook.imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .centerCrop()
            .fit()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img_item_for_you)
    }
}
