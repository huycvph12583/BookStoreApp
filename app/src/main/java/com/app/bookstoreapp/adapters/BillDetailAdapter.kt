package com.app.bookstoreapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.bookstoreapp.R
import com.app.bookstoreapp.databinding.ItemBillDetailBinding
import com.app.bookstoreapp.models.BillDetail
import com.app.bookstoreapp.models.Book
import com.squareup.picasso.Picasso

class BillDetailAdapter(private val context: Context, private val mList: List<BillDetail>, private var onItemClicked: ((bill: BillDetail) -> Unit)) :
    RecyclerView.Adapter<BillDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemBillDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemBook: BillDetail = mList[position]
        holder.setView(itemBook)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(private val itemBinding: ItemBillDetailBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun setView(itemBillDetail: BillDetail) {
            itemBinding.apply {
                Picasso.get().load(itemBillDetail.imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .centerCrop()
                    .fit()
                    .into(imgItemBillDetail)
                tvQuantity.text = itemBillDetail.quantity.toString()
                tvNameBillDetail.text = itemBillDetail.name
                tvTitleBillDetail.text = "Sách"
                tvSumPrice.text = "RS. ${itemBillDetail.price}"
                root.setOnClickListener {
                    onItemClicked(itemBillDetail)
                }
            }
        }
    }
}
