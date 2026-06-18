package com.example.colocmeal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.colocmeal.ui.auth.SignInScreen
import com.example.colocmeal.ui.auth.SignUpScreen
import com.example.colocmeal.ui.splash.SplashScreen

@Composable
fun ColocMealNavHost(rootViewModel: RootViewModel = viewModel(factory = RootViewModel.Factory))  {
    val rootState by rootViewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    when (rootState) {
        RootState.Loading -> SplashScreen()           // centered CircularProgressIndicator
        else -> {
            val start = when (rootState) {
                RootState.SignedOut       -> Routes.AuthGraph.route
                is RootState.NeedsHouse   -> Routes.HouseSetup.route
                is RootState.Ready        -> Routes.Home.route
                RootState.Loading         -> Routes.AuthGraph.route // unreachable
            }
            NavHost(navController, startDestination = start) {
                navigation(startDestination = Routes.SignIn.route, route = Routes.AuthGraph.route) {
                    composable(Routes.SignIn.route) {
                        SignInScreen(onNavigateToSignUp = { navController.navigate(Routes.SignUp.route) })
                    }
                    composable(Routes.SignUp.route) {
                        SignUpScreen(onNavigateToSignIn = { navController.popBackStack() })
                    }
                }
                composable(Routes.HouseSetup.route) { /* step 4 */ }
                composable(Routes.Home.route)       { /* steps 5–7 bottom-nav scaffold */ }
            }
        }
    }
}