package com.example.mylibrary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylibrary.R
import com.example.mylibrary.activities.SearchArchiveActivity
import com.example.mylibrary.database.RealTimeDataBase
import com.example.mylibrary.models.Archive

open class ArchiveItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Archive>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_archive, parent, false))
    }

    //creates an archive item with image, title, author and rating
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_book)
                .into(holder.itemView.findViewById(R.id.iv_book_image))

            holder.itemView.findViewById<TextView>(R.id.tv_item_title).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tv_item_author).text = model.author
            holder.itemView.findViewById<RatingBar>(R.id.tv_item_rate).rating = model.rating.toFloat()

            //gives on click functionality to the item
            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    /**
     * gets the elements from the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A function for OnClickListener where the Interface is the expected parameter..
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Archive)
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)


    fun notifyAddItem(activity: SearchArchiveActivity, position: Int){
        RealTimeDataBase().addFromArchiveToBooks(activity, list[position].documentId, list[position].isbn)
        notifyItemChanged(position)
    }

    fun searchBookList(searchList: ArrayList<Archive>){
        list = searchList
        notifyDataSetChanged()
    }
}