package com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    val name:String = "",
    val amount: Float = 0f,
    val measuremnts: String = "",
    val recipeId: Long = 0
)

data class IngredientsOfRecipe(
    @Embedded val recipe: Recipe,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId",
        associateBy = Junction(IngrientRecipe::class)
    )
    val ingredients: List<Ingredient>
)

@Entity(
    primaryKeys = ["IngredientId", "RecipeId"],
    foreignKeys =   [
        ForeignKey(entity = Recipe::class, parentColumns = ["id"], childColumns = ["RecipeId"] ),
        ForeignKey(entity = Ingredient::class, parentColumns = ["id"], childColumns = ["IngredientId"])
    ]

)
data class IngrientRecipe(
    val IngredientId:Long,
    val RecipeId:Long
)