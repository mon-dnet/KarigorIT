package com.example.karigorit.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.karigorit.R

import com.example.karigorit.Results
import java.util.*

class UserListAdapter (var context: Context, var onuser : onuserCallback):RecyclerView.Adapter<UserListAdapter.CustomuserView>(){

    var userList :MutableList<Results> = ArrayList()

    fun add(userData: Results){
        userList.add(userData)
        notifyItemChanged(userList.size-1)
        notifyDataSetChanged()
    }

    fun addAll(listuser: List<Results>){
        userList = ArrayList()
        userList.addAll(listuser)
        notifyDataSetChanged()
    }

    fun clearAll(){
        userList.clear()
        notifyDataSetChanged()
    }

    inner class CustomuserView(view: View) : RecyclerView.ViewHolder(view){
        var name = itemView.findViewById<TextView>(R.id.name_itemview)
        var userCreateDate = itemView.findViewById<TextView>(R.id.create_date)
        var image = itemView.findViewById<ImageView>(R.id.view_image)
        var view = itemView
    }


    interface onuserCallback{
        fun onuserClick(userData: Results)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomuserView {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent ,false)
        return CustomuserView(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomuserView, position: Int) {
        var item = userList.get(position)
        holder.name.text =  item.name!!.title.toString() + " "+ item.name!!.first.toString() + " " + item.name!!.last.toString()
        holder.userCreateDate.text = item.location!!.country.toString()
        Glide.with(context).load(item.picture!!.medium).diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.image)

        holder.view.setOnClickListener {
            onuser.onuserClick(item)
        }

    }
}