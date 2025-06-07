package com.outfitgo.store.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.outfitgo.store.domain.model.Collection
import kotlinx.serialization.Serializable

sealed class AppScreens {
}
@Serializable
data object LoginRoute

@Serializable
data object SignupRoute

@Serializable
data object HomeRoute

@Serializable
data object CategoriesRoute

@Serializable
data object CartRoute

@Serializable
data object SettingsRoute

@Serializable
data object CurrencySettingsRoute

@Serializable
data object ReviewsRoute

@Serializable
data class ProductDetailsRoute(val productId: String)

@Serializable
data class BrandProductsRoute(val brandId: String)

@Serializable
data class CategoryProductsRoute(val categoryHandle: String, val categoryName: String)

object SplashRoute

@Serializable
object SearchRoute

@Serializable
object RegisterRoute

data class TopLevelRoute<T : Any>(
    val title: String,
    val route: T,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
)

val topLevelRoutes = listOf(
    TopLevelRoute(
        title = "Home",
        route = HomeRoute,
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home
    ),
    TopLevelRoute(
        title = "Categories",
        route = CategoriesRoute,
        selectedIcon = Icons.Filled.Category,
        unSelectedIcon = Icons.Outlined.Category
    ),
    TopLevelRoute(
        title = "Cart",
        route = CartRoute,
        selectedIcon = Icons.Filled.ShoppingCart,
        unSelectedIcon = Icons.Outlined.ShoppingCart
    ),
    TopLevelRoute(
        title = "Profile",
        route = SettingsRoute,
        selectedIcon = Icons.Filled.Person,
        unSelectedIcon = Icons.Outlined.Person
    ),
)
