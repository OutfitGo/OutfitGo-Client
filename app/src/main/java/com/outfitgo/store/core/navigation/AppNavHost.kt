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
import com.outfitgo.store.domain.model.Address
import com.outfitgo.store.domain.model.ReviewUtils
import com.outfitgo.store.presentation.address.AddAddressScreen
import com.outfitgo.store.presentation.brandproducts.BrandProductsScreen
import com.outfitgo.store.presentation.cart.CartScreen
import com.outfitgo.store.presentation.categories.CategoriesScreen
import com.outfitgo.store.presentation.categoryproducts.CategoryProductsScreen
import com.outfitgo.store.presentation.home.HomeScreen
import com.outfitgo.store.presentation.login.LoginScreen
import com.outfitgo.store.presentation.login.LoginViewModel
import com.outfitgo.store.presentation.productdetails.ProductDetailsViewModel
import com.outfitgo.store.presentation.productdetails.ReviewsScreen
import com.outfitgo.store.presentation.register.RegisterScreen
import com.outfitgo.store.presentation.search.SearchScreen
import com.outfitgo.store.presentation.address.AddressScreen
import com.outfitgo.store.presentation.address.UpdateAddressScreen
import com.outfitgo.store.presentation.mappiker.MapPickerScreen
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
                },
                onNavToAddressScreen = {
                    navController.navigate(AddressRoute)
                }
            )
        }

        composable<AddressRoute> {
            AddressScreen(
                onEditAddress = { address ->
                    navController.navigate(
                        UpdateAddressRoute(
                            address.id,
                            address.firstName,
                            address.lastName,
                            address.line,
                            address.city,
                            address.isDefault,
                            null,
                            null
                        )
                    )
                },
                onNavToAddAddressScreen = {
                    navController.navigate(AddAddressRoute(null, null, null, null))
                }
            )
        }

        composable<AddAddressRoute> {
            val entry = it.toRoute<AddAddressRoute>()
            AddAddressScreen(
                first=entry.first,
                last=entry.last,
                onBack = {
                    navController.popBackStack()
                },
                onPickFromMap = { first, last ->
                    navController.navigate(MapPickRoute(
                        "add",
                        "",
                        first, last,
                        "", "", false
                    ))
                },
                lineFromMap = entry.line,
                cityFromMap = entry.city
            )
        }
        composable<UpdateAddressRoute> {
            val entry = it.toRoute<UpdateAddressRoute>()
            UpdateAddressScreen(
                onBack = {
                    navController.popBackStack()
                },
                onPickFromMap = {
                    navController.navigate(
                        MapPickRoute(
                            "update",
                            addressId = entry.addressId,
                            addressFirstName = entry.addressFirstName,
                            addressLastName = entry.addressLastName,
                            addressLine = entry.addressLine,
                            addressCity = entry.addressCity,
                            addressIsDefault = entry.addressIsDefault
                        )
                    )
                },
                lineFromMap = entry.line,
                cityFromMap = entry.city,
                addressId = entry.addressId,
                addressFirstName = entry.addressFirstName,
                addressLastName = entry.addressLastName,
                addressLine = entry.addressLine,
                addressCity = entry.addressCity,
                addressIsDefault = entry.addressIsDefault,
            )
        }
        composable<MapPickRoute> {
            val entry = it.toRoute<MapPickRoute>()
            MapPickerScreen(
                viewModel = hiltViewModel(),
                initialLat = 30.071941,
                initialLong = 31.018529,
                onSaveClicked = { line, city ->
                    if (entry.source == "add") {
                        navController.navigate(AddAddressRoute(entry.addressFirstName,entry.addressLastName,line, city)) {
                            popUpTo(AddressRoute) {
                                inclusive = false
                            }
                        }
                    } else {
                        navController.navigate(UpdateAddressRoute(
                            addressLine = entry.addressLine,
                            addressCity = entry.addressCity,
                            addressIsDefault = entry.addressIsDefault,
                            line = line,
                            city = city,
                            addressId = entry.addressId,
                            addressFirstName = entry.addressFirstName,
                            addressLastName = entry.addressLastName
                        )) {
                            popUpTo(AddressRoute) {
                                inclusive = false
                            }
                        }
                    }
                },
                onNavigateUp = {
                    navController.popBackStack()
                }
            )
        }

        composable<CurrencySettingsRoute> {
            CurrencyScreen()
        }

        composable<ReviewsRoute> {
            val reviews = ReviewUtils.generateRandomReviews(6)
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
                }
            )
        }

    }


}