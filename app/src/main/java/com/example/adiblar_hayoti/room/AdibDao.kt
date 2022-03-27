package com.example.adiblar_hayoti.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AdibDao {
    @Query("select * from adib_entity")
    fun getAllAdib(): List<Adib_Entity>

    @Insert
    fun addKurs(adibEntity: Adib_Entity)



    @Query("DELETE FROM adib_entity WHERE ismi=:name")
    fun  deleteByName(name:String)
}