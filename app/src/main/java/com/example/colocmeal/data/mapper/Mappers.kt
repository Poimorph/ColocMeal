package com.example.colocmeal.data.mapper

import com.example.colocmeal.data.local.entity.GroceryItemEntity
import com.example.colocmeal.data.local.entity.HouseEntity
import com.example.colocmeal.data.local.entity.IngredientEntity
import com.example.colocmeal.data.local.entity.MealPlanEntity
import com.example.colocmeal.data.local.entity.RecipeEntity
import com.example.colocmeal.data.local.entity.UserEntity
import com.example.colocmeal.data.remote.dto.GroceryItemDto
import com.example.colocmeal.data.remote.dto.HouseDto
import com.example.colocmeal.data.remote.dto.IngredientDto
import com.example.colocmeal.data.remote.dto.MealPlanDto
import com.example.colocmeal.data.remote.dto.RecipeDto
import com.example.colocmeal.data.remote.dto.UserDto
import com.example.colocmeal.domain.model.Aisle
import com.example.colocmeal.domain.model.GroceryItem
import com.example.colocmeal.domain.model.House
import com.example.colocmeal.domain.model.Ingredient
import com.example.colocmeal.domain.model.MealPlan
import com.example.colocmeal.domain.model.Recipe
import com.example.colocmeal.domain.model.Source
import com.example.colocmeal.domain.model.User
import java.time.LocalDate


// User
fun UserEntity.toDomain(): User = User(
    uid = uid,
    displayName = displayName,
    email = email,
    houseId = houseId
)

fun User.toEntity(): UserEntity = UserEntity(
    uid = uid,
    displayName = displayName,
    email = email,
    houseId = houseId
)

fun UserDto.toDomain(): User= User(
    uid = uid,
    displayName = displayName,
    email = email,
    houseId = (if (houseId !="") houseId else null)
)

fun User.toDto(): UserDto = UserDto(
    uid = uid,
    displayName = displayName,
    email = email,
    houseId = (houseId?.toString() ?: "")
)

// House
fun HouseEntity.toDomain(): House = House(
    id = id,
    name = name,
    inviteCode = inviteCode,
    creatorId = creatorId,
    memberIds = memberIds
)

fun House.toEntity(): HouseEntity = HouseEntity(
    id = id,
    name = name,
    inviteCode = inviteCode,
    creatorId = creatorId,
    memberIds = memberIds
)

fun HouseDto.toDomain() : House = House(
    id = id,
    name = name,
    inviteCode = inviteCode,
    creatorId = creatorId,
    memberIds = memberIds
)

fun House.toDto() : HouseDto = HouseDto(
    id = id,
    name = name,
    inviteCode = inviteCode,
    creatorId = creatorId,
    memberIds = memberIds
)

// Recipe
fun RecipeEntity.toDomain(): Recipe = Recipe(
    id = id,
    name = name,
    description = description,
    ingredients = ingredients,
    authorId = authorId,
    authorName = authorName,
    houseId = houseId,
    isShared = isShared
)

fun Recipe.toEntity(): RecipeEntity = RecipeEntity(
    id = id,
    name = name,
    description = description,
    ingredients = ingredients,
    authorId = authorId,
    authorName = authorName,
    houseId = houseId,
    isShared = isShared
)

fun RecipeDto.toDomain() : Recipe = Recipe(
    id = id,
    name = name,
    description = (if (description!= "") description else null),
    ingredients = ingredients,
    authorId = authorId,
    authorName = authorName,
    houseId = (if (houseId != "") houseId else null),
    isShared = isShared.toBoolean()
)

fun Recipe.toDto() = RecipeDto(
    id = id,
    name = name,
    description = (description ?: ""),
    ingredients = ingredients,
    authorId = authorId,
    authorName = authorName,
    houseId = (houseId ?: ""),
    isShared = isShared.toString()
    )

// Meal Plan
fun MealPlanEntity.toDomain(): MealPlan = MealPlan(
    id = id,
    houseId = houseId,
    weekStart = weekStart,
    dayOfWeek = dayOfWeek,
    recipeId = recipeId,
    recipeName = recipeName,
    cookId = cookId,
    cookName = cookName
)

fun MealPlan.toEntity(): MealPlanEntity = MealPlanEntity(
    id = id,
    houseId = houseId,
    weekStart = weekStart,
    dayOfWeek = dayOfWeek,
    recipeId = recipeId,
    recipeName = recipeName,
    cookId = cookId,
    cookName = cookName
)

fun MealPlanDto.toDomain() : MealPlan = MealPlan(
    id = id,
    houseId = houseId,
    weekStart = LocalDate.parse(weekStart),
    dayOfWeek = dayOfWeek.toInt(),
    recipeId = recipeId,
    recipeName = recipeName,
    cookId = cookId,
    cookName = cookName
)

fun MealPlan.toDto() : MealPlanDto = MealPlanDto(
    id = id,
    houseId = houseId,
    weekStart = weekStart.toString(),
    dayOfWeek = dayOfWeek.toString(),
    recipeId = recipeId,
    recipeName = recipeName,
    cookId = cookId,
    cookName = cookName
)

// GroceryItem
fun GroceryItemEntity.toDomain(): GroceryItem = GroceryItem(
    id = id,
    houseId = houseId,
    name = name,
    nameNormalized = nameNormalized,
    aisle = aisle,
    isChecked = isChecked,
    checkedByName = checkedByName,
    source = source,
    addedBy = addedBy
)

fun GroceryItem.toEntity(): GroceryItemEntity = GroceryItemEntity(
    id = id,
    houseId = houseId,
    name = name,
    nameNormalized = nameNormalized,
    aisle = aisle,
    isChecked = isChecked,
    checkedByName = checkedByName,
    source = source,
    addedBy = addedBy
)

fun GroceryItemDto.toDomain(): GroceryItem = GroceryItem(
    id = id,
    houseId = houseId,
    name = name,
    nameNormalized = nameNormalized,
    aisle = Aisle.valueOf(aisle),
    isChecked = isChecked,
    checkedByName = checkedByName,
    source = Source.valueOf(source),
    addedBy = addedBy
)

fun GroceryItem.toDto(): GroceryItemDto = GroceryItemDto(
    id = id,
    houseId = houseId,
    name = name,
    nameNormalized = nameNormalized,
    aisle = aisle.name,
    isChecked = isChecked,
    checkedByName = checkedByName,
    source = source.name,
    addedBy = addedBy
)

// Ingredient
fun IngredientEntity.toDomain(): Ingredient = Ingredient(
    id = id,
    houseId = houseId,
    name = name,
    nameNormalized = nameNormalized,
    aisle = aisle
)

fun Ingredient.toEntity(): IngredientEntity = IngredientEntity(
    id = id,
    houseId = houseId,
    name = name,
    nameNormalized = nameNormalized,
    aisle = aisle
)

fun IngredientDto.toDomain(): Ingredient = Ingredient(
    id = id,
    houseId = houseId,
    name = name,
    nameNormalized = nameNormalized,
    aisle = Aisle.valueOf(aisle)
)

fun Ingredient.toDto(): IngredientDto = IngredientDto(
    id = id,
    houseId = houseId,
    name = name,
    nameNormalized = nameNormalized,
    aisle = aisle.name
)
