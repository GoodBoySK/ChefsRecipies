package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Instruction
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Tag

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Tag)
    @Delete
    suspend fun delete(item: Tag)
    @Transaction
    @Query("SELECT * FROM Tag WHERE recipeId = :recipeId")
    fun getTags(recipeId: Long) : List<Tag>
}