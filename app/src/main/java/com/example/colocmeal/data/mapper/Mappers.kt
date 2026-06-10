package com.example.colocmeal.data.mapper

import com.example.colocmeal.data.local.entity.GroceryItemEntity
import com.example.colocmeal.data.local.entity.HouseEntity
import com.example.colocmeal.data.local.entity.MealPlanEntity
import com.example.colocmeal.data.local.entity.RecipeEntity
import com.example.colocmeal.data.local.entity.UserEntity
import com.example.colocmeal.data.remote.dto.GroceryItemDto
import com.example.colocmeal.domain.model.Aisle
import com.example.colocmeal.domain.model.GroceryItem
import com.example.colocmeal.domain.model.House
import com.example.colocmeal.domain.model.MealPlan
import com.example.colocmeal.domain.model.Recipe
import com.example.colocmeal.domain.model.Source
import com.example.colocmeal.domain.model.User

// TODO 0 : Add Dto <-> Domain mappers

fun UserEntity.toDomain(): User = User(
    uid = id,
    displayName = displayName,
    email = email,
    houseId = houseId
)

fun User.toEntity(): UserEntity = UserEntity(
    id = uid,
    displayName = displayName,
    email = email,
    houseId = houseId
)

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

fun GroceryItemEntity.toDomain(): GroceryItem = GroceryItem(
    id = id,
    houseId = houseId,
    name = name,
    nameNormalized = nameNormalized,
    aisle = aisle,
    isChecked = isChecked,
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
    source = Source.valueOf(source),
    addedBy = addedBy
)

fun GroceryItem.toDto(): GroceryItemDto = GroceryItemDto(
    id = id,
    houseId = houseId,
    name = name,
    nameNormalized = nameNormalized,
    aisle = aisle.displayName,
    isChecked = isChecked,
    source = source.name,
    addedBy = addedBy
)
