package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Ingredient
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Instruction
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Tag
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.IngredientDao
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.InstructionDao
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.RecipeDao
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.daos.TagDao

@Database(entities = [Recipe::class, Ingredient::class, Instruction::class, Tag::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun instructionDao(): InstructionDao
    abstract fun tagDao(): TagDao
    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            //synchroniczed = can be run only by 1 thread at a time
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "ChefsRecipeDatabase")
                    .build()
                    .also { Instance = it }
            }
        }
    }


}