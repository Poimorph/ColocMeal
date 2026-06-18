package com.example.colocmeal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.colocmeal.ColocMealApplication
import com.example.colocmeal.ui.auth.SignInScreen
import com.example.colocmeal.ui.auth.SignUpScreen
import com.example.colocmeal.ui.home.HomeScreen
import com.example.colocmeal.ui.house.HouseSetupScreen
import com.example.colocmeal.ui.splash.SplashScreen

@Composable
fun ColocMealNavHost(rootViewModel: RootViewModel = viewModel(factory = RootViewModel.Factory))  {
    val rootState by rootViewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    when (rootState) {
        RootState.Loading -> SplashScreen()
        else -> {
            val start = when (rootState) {
                RootState.SignedOut       -> Routes.AuthGraph.route
                is RootState.NeedsHouse   -> Routes.HouseSetup.route
                is RootState.Ready        -> Routes.Home.route
                RootState.Loading         -> Routes.AuthGraph.route
            }
            LaunchedEffect(start) {
                if (navController.currentDestination?.route != start) {
                    navController.navigate(start) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
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
                composable(Routes.HouseSetup.route) {
                    HouseSetupScreen()
                }
                composable(Routes.Home.route)       {
                    val ready = rootState as? RootState.Ready
                    if (ready != null) {
                        val container = (LocalContext.current.applicationContext as ColocMealApplication).container
                        LaunchedEffect(ready.houseId) { container.houseSession.start(ready.houseId) }
                        HomeScreen(houseId = ready.houseId)
                    }
                }
            }
        }
    }
}