package com.outfitgo.store.core.navigation

import ProductDetailsScreen
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.outfitgo.store.domain.model.ReviewUtils
import com.outfitgo.store.presentation.brandproducts.BrandProductsScreen
import com.outfitgo.store.presentation.cart.CartScreen
import com.outfitgo.store.presentation.categories.CategoriesScreen
import com.outfitgo.store.presentation.categoryproducts.CategoryProductsScreen
import com.outfitgo.store.presentation.home.HomeScreen
import com.outfitgo.store.presentation.login.LoginScreen
import com.outfitgo.store.presentation.login.LoginViewModel
import com.outfitgo.store.presentation.pending.PendingScreen
import com.outfitgo.store.presentation.productdetails.ProductDetailsViewModel
import com.outfitgo.store.presentation.productdetails.ReviewsScreen
import com.outfitgo.store.presentation.register.RegisterScreen
import com.outfitgo.store.presentation.search.SearchScreen
import com.outfitgo.store.presentation.settings.view.CurrencyScreen
import com.outfitgo.store.presentation.settings.view.SettingsScreen
import com.outfitgo.store.presentation.splash.OutfitGoSplashScreen
import com.outfitgo.store.presentation.wishlist.WishlistScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier,
        enterTransition = { fadeIn(tween(600)) + slideInHorizontally(tween(600)) },
        exitTransition = {
            fadeOut(tween(600)) + slideOutHorizontally(
                tween(600),
                targetOffsetX = { it / 2 })
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
                },
                onGoToSignup = {
                    navController.navigate(RegisterRoute)
                }
            )
        }

        composable<HomeRoute> {
            HomeScreen(
                onNavigateToBrandProducts = {
                    navController.navigate(BrandProductsRoute(it))
                }, onNavigateToProductDetails = {
                    navController.navigate(ProductDetailsRoute(it.id))
                }, onNavigateToSearchScreen = {
                    navController.navigate(SearchRoute)
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
        composable<CartRoute> {
            CartScreen()
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
                onNavigateUp = { navController.navigateUp() },
                onShowMoreReviewsClicked = {
                    navController.navigate(ReviewsRoute)
                }
            )
        }

        composable<SettingsRoute> {
            SettingsScreen(
                onNavToCurrencySettings = {
                    navController.navigate(CurrencySettingsRoute)
                },
                onNavToWishlistScreen = {
                    navController.navigate(WishlistRoute)
                }
            )
        }

        composable<CurrencySettingsRoute> {
            CurrencyScreen()
        }

        composable<ReviewsRoute> {
            val reviews = remember { ReviewUtils.generateRandomReviews() }
            ReviewsScreen(
                reviews = reviews,
                onNavigateUp = { navController.navigateUp() },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable<SplashRoute> {
            OutfitGoSplashScreen(
                viewModel = hiltViewModel(),
                modifier = Modifier.fillMaxSize(),
                onGoToHome = { navController.navigate(HomeRoute) },
                onGoToLogin = { navController.navigate(LoginRoute) }
            )
        }

        composable<SearchRoute> {
            SearchScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateUp = { navController.navigateUp() },
                onNavigateToProductDetails = { productId ->
                    navController.navigate(ProductDetailsRoute(productId))
                }
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                modifier = Modifier.fillMaxSize(),
                onContinueAsGuestClicked = {
                    navController.navigate(HomeRoute)
                },
                onGoToLoginClicked = {
                    navController.navigate(LoginRoute)
                },
                onGoToHome = {
                    navController.navigate(HomeRoute)
                },
                onGoToPending = { email, password, firstname, lastname ->
                    navController.navigate(
                        PendingRoute(
                            email = email,
                            password = password,
                            firstName = firstname,
                            lastName = lastname
                        )
                    )
                }
            )
        }

        composable<WishlistRoute> {
            WishlistScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateUp = {
                    navController.navigateUp()
                },
                onGoToProductDetails = { productId ->
                    navController.navigate(ProductDetailsRoute(productId))
                },
            )
        }

        composable<PendingRoute> {
            val route = it.toRoute<PendingRoute>()
            PendingScreen(
                email = route.email,
                password = route.password,
                firstName = route.firstName,
                lastName = route.lastName,
                onGoToHome = { navController.navigate(HomeRoute) },
                modifier = Modifier.fillMaxSize(),
            )
        }

    }


}