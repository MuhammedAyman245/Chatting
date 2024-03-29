package com.example.chatapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.activity.ChatActivity
import com.example.chatapp.activity.UserActivity
import com.example.chatapp.model.User

class UserAdapter(private val context: UserActivity, private val userList:ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.userNameTxt.text = user.userName
        Glide.with(context).load(user.userImg).into(holder.imguser)
        holder.layoutUser.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("UserId", user.userId)
            context.startActivity(intent)
        }
    }

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val userNameTxt:TextView = view.findViewById(R.id.userNameTv)
        val txtDummyTxt: TextView= view.findViewById(R.id.dummyTextTv)
        val imguser: ImageView = view.findViewById(R.id.userImageIv)
        val layoutUser: LinearLayout = view.findViewById(R.id.layoutUser)
    }
}