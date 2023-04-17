package com.app.bookstoreapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.bookstoreapp.R
import com.app.bookstoreapp.databinding.ItemCartBookBinding
import com.app.bookstoreapp.models.Cart
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class CartAdapter(
    private val uid: String,
    private val mList: List<Cart>,
    private var onItemClicked: ((cart: Cart, ImageView, ImageView) -> Unit)
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemCartBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemBook: Cart = mList[position]
        holder.setView(itemBook)
    }

    override fun getItemCount(): Int {
        return mList.size
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val itemBinding: ItemCartBookBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun setView(itemCart: Cart) {
            itemBinding.apply {
                val database =
                    FirebaseDatabase.getInstance().getReference("Cart").child(uid)
                database.child(itemCart.idFood)
                Picasso.get().load(itemCart.imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .centerCrop()
                    .fit()
                    .into(imgItemCart)
                tvCartNameBook.text = itemCart.name
                tvQuantity.text = itemCart.quantity.toString()
                tvSumPrice.text = "$ ${itemCart.price * tvQuantity.text.toString().toInt()}"
                imgAddQuantity.setOnClickListener {
                    val userUpdates: MutableMap<String, Any> = HashMap()
                    userUpdates["${itemCart.idFood}/quantity"] = itemCart.quantity + 1
                    database.updateChildren(userUpdates)

                }
                imgRemoveQuantity.setOnClickListener {
                    if (itemCart.quantity == 1) {
                        database.child(itemCart.idFood).removeValue()
                    } else {
                        val userUpdates: MutableMap<String, Any> = HashMap()
                        userUpdates["${itemCart.idFood}/quantity"] = itemCart.quantity - 1
                        database.updateChildren(userUpdates)
                    }
                }
            }
        }
    }
}
