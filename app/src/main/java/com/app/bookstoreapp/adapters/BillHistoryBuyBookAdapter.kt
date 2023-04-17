package com.app.bookstoreapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.bookstoreapp.databinding.ItemHistoryBuyBookBinding
import com.app.bookstoreapp.models.Bill

class BillHistoryBuyBookAdapter(
    private val context: Context,
    private val mList: List<Bill>,
    private var onItemClicked: ((bill: Bill) -> Unit)
) :
    RecyclerView.Adapter<BillHistoryBuyBookAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemHistoryBuyBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemBook: Bill = mList[position]
        holder.setView(itemBook)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(private val itemBinding: ItemHistoryBuyBookBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun setView(itemBill: Bill) {
            itemBinding.apply {
                tvIdBill.text = "Code Bill:${itemBill.idBill}"
                tvDateBill.text = "Date Buy: ${itemBill.date}"
                tvQuantityBill.text = "Sum Quantity: ${itemBill.sumQuantity}"
                tvSumPrice.text = "Sum Money: ${itemBill.sumPrice}"
                root.setOnClickListener {
                    onItemClicked(itemBill)
                }
            }
        }
    }
}
