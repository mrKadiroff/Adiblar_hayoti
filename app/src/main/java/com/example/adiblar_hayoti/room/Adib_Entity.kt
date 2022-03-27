package com.example.adiblar_hayoti.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Adib_Entity :Serializable{
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "photo")
    var photoUrl: String? = null

    @ColumnInfo(name = "ismi")
    var name: String? = null

    @ColumnInfo(name = "birth")
    var birth_date: String? = null

    @ColumnInfo(name = "death")
    var death_date: String? = null

    @ColumnInfo(name = "tur")
    var type: String? = null

    @ColumnInfo(name = "dsecr")
    var description: String? = null

}