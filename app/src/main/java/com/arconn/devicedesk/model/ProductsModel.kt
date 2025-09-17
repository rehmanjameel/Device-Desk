package com.arconn.devicedesk.model

import android.accessibilityservice.GestureDescription
import java.math.BigDecimal

class ProductsModel(
    val id: Int,
    val name: String,
    val price: BigDecimal,
    val description: String,
    val categoryId: Int
)