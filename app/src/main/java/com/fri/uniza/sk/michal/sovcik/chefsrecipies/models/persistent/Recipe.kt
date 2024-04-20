package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation


enum class DishType{
    MainDish,
    Breakfast,
    Dezert,
    Soup
}

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id:Long = -1,
    val name:String = "",
    val dishType: DishType = DishType.Soup,
    val time:Int = 0,
    val portions:Int = 0,
    val rating: Float = 0f,
    val autor: String = "",
    val description: String = "",
    val picPath: String? = null
    )

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id:Long = -1,
    val name:String = "",
    val amount: Float = 0f,
    val measuremnts: String = "",
    val recipeId: Long = 0
)
data class IngredientsOfRecipe(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<Ingredient>
)



@Entity(primaryKeys = ["recipeId","tag"])
data class Tag(
    val recipeId: Long,
    val tag:String
)
data class TagsOfRecipe(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<Tag>
)



@Entity
data class Instruction(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val orderNum:Int,
    val text:String,
    val recipeId: Long,
    val imgPath:String?
)
data class InstructionsOfRecipe(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<Instruction>
)