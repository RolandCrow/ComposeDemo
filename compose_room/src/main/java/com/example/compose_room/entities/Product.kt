package com.example.compose_room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.PropertyKey

@Entity(tableName = "products")
class Product {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "productId")
    var id: Int = 0

    @ColumnInfo(name = "productName")
    var productName: String = ""
    var quantity: Int = 0

    constructor()

    constructor(productName:String, quantity: Int) {
        this.productName = productName
        this.quantity = quantity
    }
}