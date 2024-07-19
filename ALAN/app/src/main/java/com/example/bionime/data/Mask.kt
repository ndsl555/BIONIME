package com.example.bionime.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "masks")
data class Mask(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val maskAdult: Int,
    val maskChild: Int,
    val town: String
)