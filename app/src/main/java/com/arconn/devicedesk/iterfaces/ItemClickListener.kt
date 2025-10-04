package com.arconn.devicedesk.iterfaces

import com.arconn.devicedesk.model.UsersModel

interface ItemClickListener {
    fun onDeleteClick(item: UsersModel)

    fun onEditClick(item: UsersModel)

}