package com.outfitgo.store.presentation


import ProductDetailsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.outfitgo.store.core.navigation.AppNavHost
import com.outfitgo.store.core.navigation.HomeRoute
import com.outfitgo.store.core.navigation.topLevelRoutes
import com.outfitgo.store.presentation.productdetails.ProductDetailsViewModel
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination
            val topLevelRouteNames = topLevelRoutes.map {  it.route::class.qualifiedName }
            val shouldShowBottomBar = currentDestination?.route in topLevelRouteNames
            OutfitGoTheme {
                Scaffold(
                    bottomBar = {
                        var selectedIndex by remember { mutableIntStateOf(0) }
                        AnimatedVisibility(
                            visible = shouldShowBottomBar,
                            enter = fadeIn(tween(600)) + slideInVertically(tween(600)),
                            exit = fadeOut(tween(600)) + slideOutVertically(tween(600), targetOffsetY = {it/2})
                        ) {
                            BottomAppBar {
                                topLevelRoutes.forEachIndexed { index,  topLevelRoute ->
                                    NavigationBarItem(
                                        selected = currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route::class) } == true,
                                        icon = {
                                            if(index == selectedIndex) {
                                                Icon(imageVector = topLevelRoute.selectedIcon, contentDescription = topLevelRoute.title)
                                            } else {
                                                Icon(imageVector = topLevelRoute.unSelectedIcon, contentDescription = topLevelRoute.title)
                                            }
                                        },
                                        onClick = {
                                            selectedIndex = index
                                            navController.navigate(topLevelRoute.route) {
                                                popUpTo(HomeRoute)
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        label = { Text(text = topLevelRoute.title) },
                                        alwaysShowLabel = false
                                    )
                                }
                            }
                        }


                    }
                ) { innerPadding ->
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    )
                }



            }
        }

    }
}
