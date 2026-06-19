package com.example.colocmeal.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.colocmeal.data.local.dao.GroceryItemDao
import com.example.colocmeal.data.local.dao.HouseDao
import com.example.colocmeal.data.local.dao.IngredientDao
import com.example.colocmeal.data.local.dao.MealPlanDao
import com.example.colocmeal.data.local.dao.RecipeDao
import com.example.colocmeal.data.local.dao.UserDao
import com.example.colocmeal.data.local.entity.GroceryItemEntity
import com.example.colocmeal.data.local.entity.HouseEntity
import com.example.colocmeal.data.local.entity.IngredientEntity
import com.example.colocmeal.data.local.entity.MealPlanEntity
import com.example.colocmeal.data.local.entity.RecipeEntity
import com.example.colocmeal.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        HouseEntity::class,
        RecipeEntity::class,
        MealPlanEntity::class,
        GroceryItemEntity::class,
        IngredientEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun houseDao(): HouseDao
    abstract fun recipeDao(): RecipeDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun groceryItemDao(): GroceryItemDao
    abstract fun ingredientDao(): IngredientDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "colocmeal.db"
                ).fallbackToDestructiveMigration(true).build().also { instance = it }
            }
        }
    }
}
