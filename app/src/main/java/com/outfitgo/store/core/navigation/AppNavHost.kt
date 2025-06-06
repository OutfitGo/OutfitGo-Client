package com.outfitgo.store.core.navigation

import ProductDetailsScreen
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.outfitgo.store.presentation.brandproducts.BrandProductsScreen
import com.outfitgo.store.presentation.categories.CategoriesScreen
import com.outfitgo.store.presentation.categoryproducts.CategoryProductsScreen
import com.outfitgo.store.presentation.home.HomeScreen
import com.outfitgo.store.presentation.login.LoginScreen
import com.outfitgo.store.presentation.login.LoginViewModel
import com.outfitgo.store.presentation.productdetails.ProductDetailsViewModel
import com.outfitgo.store.presentation.settings.view.CurrencyScreen
import com.outfitgo.store.presentation.settings.view.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = LoginRoute,
        modifier = modifier,
        enterTransition = { fadeIn(tween(600)) + slideInVertically(tween(600)) },
        exitTransition = {
            fadeOut(tween(600)) + slideOutVertically(
                tween(600),
                targetOffsetY = { it / 2 })
        }
    ) {

        composable<LoginRoute> {
            val viewmodel: LoginViewModel = hiltViewModel()
            val loginState = viewmodel.state.collectAsStateWithLifecycle()
            LoginScreen(
                state = loginState.value,
                onIntent = viewmodel::processIntent,
                effectFlow = viewmodel.effect,
                modifier = Modifier.fillMaxSize(),
                onGoToHome = {
                    navController.navigate(HomeRoute)
                }
            )
        }

        composable<HomeRoute> {
            HomeScreen(
                onNavigateToBrandProducts = {
                    navController.navigate(BrandProductsRoute(it))
                }, onNavigateToProductDetails = {
                    navController.navigate(ProductDetailsRoute(it.id))
                }
            )
        }

        composable<CategoriesRoute> {
            CategoriesScreen(
                onNavigateToCategoryProducts = { category ->
                    Log.d("```TAG```", "AppNavHost: Clicked")
                    navController.navigate(
                        CategoryProductsRoute(
                            categoryHandle = category.handle,
                            categoryName = category.name
                        )
                    )
                }
            )
        }

        composable<CategoryProductsRoute> {
            val entry = it.toRoute<CategoryProductsRoute>()
            CategoryProductsScreen(
                categoryName = entry.categoryName,
                categoryHandle = entry.categoryHandle,
                onNavigateToProductDetails = { product ->
                    navController.navigate(ProductDetailsRoute(product.id))
                },
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }

        composable<BrandProductsRoute> {
            val entry = it.toRoute<BrandProductsRoute>()
            BrandProductsScreen(
                brand = entry.brandId,
                onNavigateUp = {
                    navController.navigateUp()
                },
                onNavigateToProductDetails = {
                    navController.navigate(ProductDetailsRoute(it.id))
                }
            )
        }

        composable<ProductDetailsRoute> {
            val entry = it.toRoute<ProductDetailsRoute>()
            val viewModel: ProductDetailsViewModel = hiltViewModel()
            val state = viewModel.state.collectAsStateWithLifecycle()
            ProductDetailsScreen(
                productId = entry.productId,
                state = state.value,
                onIntent = viewModel::processIntent,
                effect = viewModel.effect,
                modifier = Modifier.fillMaxSize(),
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable<SettingsRoute> {
            SettingsScreen {
                navController.navigate(CurrencySettingsRoute)
            }
        }

        composable<CurrencySettingsRoute> {
            CurrencyScreen()
        }

    }


}