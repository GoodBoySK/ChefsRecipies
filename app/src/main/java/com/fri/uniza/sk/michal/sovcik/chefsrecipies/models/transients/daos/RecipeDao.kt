package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe:Recipe) : Long
    @Update
    suspend fun update(recipe:Recipe)

    @Delete
    suspend fun delete(recipe:Recipe)

    @Query("SELECT * from recipe where name = :name limit 1")
    fun getRecipe(name:String) : Flow<Recipe>
    @Query("SELECT * from recipe where id = :id")
    fun getRecipe(id:Long) : Flow<Recipe>

    @Query("SELECT * from recipe ORDER BY name ASC")
    fun getAllRecipies(): Flow<List<Recipe>>

    //@Query("SELECT * from recipe WHERE id in (SELECT recipeId FROM tag WHERE tag in (:tags)) AND id IN (SELECT recipeId FROM Ingredient where id IN (:ingredients)) ORDER BY name ASC")
    @Query("SELECT * FROM recipe WHERE id IN ( SELECT recipeId FROM tag WHERE tag IN (:tags) INTERSECT SELECT recipeId FROM Ingredient WHERE name IN (:ingredients)) ORDER BY name ASC")
    fun getAllRecipies(tags: List<String>,ingredients:List<String>): Flow<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE id IN ( SELECT recipeId FROM tag WHERE tag IN (:tags)) ORDER BY name ASC")
    fun getAllRecipiesTags(tags: List<String>): Flow<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE id IN (SELECT recipeId FROM Ingredient WHERE name IN (:ingredients)) ORDER BY name ASC")
    fun getAllRecipiesIngredients(ingredients:List<String>): Flow<List<Recipe>>


    @Query("SELECT * from recipe WHERE dishType = :dishType ORDER BY name ASC")
    fun getAllRecipies(dishType: DishType): Flow<List<Recipe>>

}