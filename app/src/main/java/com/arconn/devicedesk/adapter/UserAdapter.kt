package com.arconn.devicedesk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arconn.devicedesk.R
import com.arconn.devicedesk.iterfaces.ItemClickListener
import com.arconn.devicedesk.model.UsersModel

class UserAdapter(
    private val context: Context,
    private val usersList: List<UsersModel>,
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    var listener : ItemClickListener ?= null


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

        holder.userName?.text = item.name
        holder.userEmail?.text = item.email

        holder.delete.setOnClickListener {
            listener?.onDeleteClick(item)
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val userName: TextView? = itemView.findViewById(R.id.userName)
        val userEmail: TextView? = itemView.findViewById(R.id.userEmail)

        val delete: ImageView = itemView.findViewById(R.id.delete)

        val editUser: ImageView = itemView.findViewById(R.id.editUser)

    }

}