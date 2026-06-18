package com.example.colocmeal.ui.navigation

sealed interface Routes {
    val route: String

    data object SignIn    : Routes { override val route = "sign_in" }
    data object SignUp    : Routes { override val route = "sign_up" }
    data object AuthGraph : Routes { override val route = "auth" }
    data object HouseSetup: Routes { override val route = "house_setup" }
    data object Home      : Routes { override val route = "home" }

    // Pattern for later steps:
    //   data object RecipeDetail : Routes {
    //       override val route = "recipe/{recipeId}"
    //       const val ARG = "recipeId"
    //       fun build(recipeId: String) = "recipe/$recipeId"
    //   }
    // The destination then reads the id from SavedStateHandle / backStackEntry
    // and loads the recipe in its ViewModel from the repo.

}
