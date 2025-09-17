package com.arconn.devicedesk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arconn.devicedesk.R
import com.arconn.devicedesk.helpers.Resource
import com.arconn.devicedesk.model.UsersModel

class UserAdapter(
    private val context: Context,
    private val usersList: List<UsersModel>
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.users_layout, parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = usersList[position]

        holder.userName.text = item.name
        holder.userEmail.text = item.email
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val userName = itemView.findViewById<TextView>(R.id.userName)
        val userEmail = itemView.findViewById<TextView>(R.id.userEmail)
    }

}