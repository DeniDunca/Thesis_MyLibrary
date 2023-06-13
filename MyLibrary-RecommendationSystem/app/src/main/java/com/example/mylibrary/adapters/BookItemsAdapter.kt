package com.example.mylibrary.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylibrary.R
import com.example.mylibrary.activities.MyBooksActivity
import com.example.mylibrary.activities.UpdateBookActivity
import com.example.mylibrary.database.RealTimeDataBase
import com.example.mylibrary.models.Book
import com.example.mylibrary.utils.Constants

open class BookItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Book>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_book, parent, false))
    }

    /**
     * creates a book element with image, title, author, rating and pages read
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_book)
                .into(holder.itemView.findViewById(R.id.iv_book_image))

            val slider = holder.itemView.findViewById<AppCompatSeekBar>(R.id.book_slider)

            holder.itemView.findViewById<TextView>(R.id.tv_item_title).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tv_item_author).text = model.author
            holder.itemView.findViewById<RatingBar>(R.id.tv_item_rate).rating = model.myRate
            slider.max = model.pages.toInt()
            if (model.pagesRead.isNotEmpty()) {
                slider.progress = model.pagesRead.toInt()
            } else {
                slider.progress = 0
            }
            slider.setOnTouchListener { _, _ -> true }
            holder.itemView.findViewById<TextView>(R.id.tv_pages_read).text = model.pagesRead
            holder.itemView.findViewById<TextView>(R.id.tv_max_pages).text = model.pages

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A function for OnClickListener where the Interface is the expected parameter.
     */
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Book)
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun notifyEditItem(activity: Activity, book: Book, requestCode: Int) {
        val intent = Intent(context, UpdateBookActivity::class.java)
        intent.putExtra(Constants.DOCUMENT_ID, book.documentId)
        activity.startActivityForResult(intent, requestCode)
        notifyDataSetChanged()
    }

    fun removeAt(activity: MyBooksActivity, position: Int) {
        RealTimeDataBase().deleteBook(activity, list[position].documentId)
        list.removeAt(position)
        notifyItemRemoved(position)
        RealTimeDataBase().getBookList(activity)
    }

    fun searchBookList(searchList: ArrayList<Book>) {
        list = searchList
        notifyDataSetChanged()
    }

    fun getBookAt(position: Int): Book? {
        return if (position >= 0 && position < list.size) {
            list[position]
        } else {
            null
        }
    }
}
