package com.example.bionime.data

import androidx.room.*

@Dao
interface MaskDao {
    @Query("SELECT * FROM masks WHERE town = :town")
    suspend fun getMasksByTown(town: String): List<Mask>

    @Query("SELECT * FROM masks")
    suspend fun getAllMasks(): List<Mask>

    //在這邊直接做不重複城市的處理
    @Query("SELECT DISTINCT town FROM masks")
    suspend fun getAllTowns(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMasks(masks: List<Mask>)

    @Query("DELETE FROM masks WHERE name = :name")
    suspend fun deleteMaskByName(name: String)
}