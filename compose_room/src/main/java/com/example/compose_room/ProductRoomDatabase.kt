package com.example.compose_room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.compose_room.dao.ProductDao
import com.example.compose_room.entities.Product

@Database(entities = [(Product::class)], version = 1)
abstract class ProductRoomDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        private var INSTANCE: ProductRoomDatabase? = null

        fun getInstance(context: Context): ProductRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProductRoomDatabase::class.java,
                        "product_database"
                    ).fallbackToDestructiveMigration().build()
                }
                return instance
            }
        }
    }
}