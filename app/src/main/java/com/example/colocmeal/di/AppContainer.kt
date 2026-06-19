package com.example.colocmeal.di

import com.example.colocmeal.data.local.AppDatabase
import android.content.Context
import com.example.colocmeal.data.remote.GroceryFirestoreDataSource
import com.example.colocmeal.data.remote.HouseFirestoreDataSource
import com.example.colocmeal.data.remote.IngredientFirestoreDataSource
import com.example.colocmeal.data.remote.MealFirestoreDataSource
import com.example.colocmeal.data.remote.RecipeFirestoreDataSource
import com.example.colocmeal.data.remote.UserFirestoreDataSource
import com.example.colocmeal.data.repository.AuthRepositoryImpl
import com.example.colocmeal.data.repository.GroceryRepositoryImpl
import com.example.colocmeal.data.repository.HouseRepositoryImpl
import com.example.colocmeal.data.repository.IngredientRepositoryImpl
import com.example.colocmeal.data.repository.MealPlanRepositoryImpl
import com.example.colocmeal.data.repository.RecipeRepositoryImpl
import com.example.colocmeal.data.repository.UserRepositoryImpl
import com.example.colocmeal.domain.repository.AuthRepository
import com.example.colocmeal.domain.repository.GroceryRepository
import com.example.colocmeal.domain.repository.HouseRepository
import com.example.colocmeal.domain.repository.IngredientRepository
import com.example.colocmeal.domain.repository.MealPlanRepository
import com.example.colocmeal.domain.repository.RecipeRepository
import com.example.colocmeal.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class AppContainer(context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val firestore = FirebaseProvider.firestore
    val auth = FirebaseProvider.auth

    val appScope = CoroutineScope(SupervisorJob()+ Dispatchers.IO)

    val userRepository : UserRepository = UserRepositoryImpl(db.userDao(), UserFirestoreDataSource(firestore), appScope)
    val houseRepository : HouseRepository = HouseRepositoryImpl(db.houseDao(),HouseFirestoreDataSource(firestore), appScope)
    val recipeRepository : RecipeRepository = RecipeRepositoryImpl(db.recipeDao(),RecipeFirestoreDataSource(firestore), appScope)
    val mealPlanRepository : MealPlanRepository = MealPlanRepositoryImpl(db.mealPlanDao(),MealFirestoreDataSource(firestore), appScope)
    val groceryRepository : GroceryRepository = GroceryRepositoryImpl(db.groceryItemDao(),GroceryFirestoreDataSource(firestore), appScope)
    val ingredientRepository : IngredientRepository = IngredientRepositoryImpl(db.ingredientDao(), IngredientFirestoreDataSource(firestore), appScope)
    val authRepository: AuthRepository = AuthRepositoryImpl(auth, userRepository)

    val houseSession = HouseSession(this)
}