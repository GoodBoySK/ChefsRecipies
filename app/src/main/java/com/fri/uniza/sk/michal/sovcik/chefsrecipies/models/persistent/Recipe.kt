package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent

import android.media.Rating
import android.net.Uri
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id:Int ,
    val name:String,
    val time:Int,
    val portions:Int,
    val rating: Float,
    val autor: String,
    val picPath: Uri
    ){

}
@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val amount: Float,
    val measuremnts: String,
    val recipeId: Int
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
    val recipeId: Int,
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
    val id:Int,
    val orderNum:Int,
    val text:String,
    val recipeId: Int,
    val imgPath:Uri?
)
data class InstructionsOfRecipe(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<Instruction>
)