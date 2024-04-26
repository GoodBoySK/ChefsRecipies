package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Tag)
    @Delete
    suspend fun delete(item: Tag)
    @Transaction
    @Query("SELECT * FROM Tag WHERE recipeId = :recipeId")
    fun getTags(recipeId: Long) : Flow<List<Tag>>

    @Transaction
    @Query("SELECT * FROM Tag WHERE tag COLLATE NOCASE LIKE :filter GROUP BY tag")
    fun getAllTags(filter:String) : Flow<List<Tag>>
}