package com.outfitgo.store.presentation.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.outfitgo.store.R
import com.outfitgo.store.domain.model.order.Order
import com.outfitgo.store.presentation.orders.OrdersIntent.GoBack
import com.outfitgo.store.presentation.orders.components.OrdersScreenHeader
import com.outfitgo.store.presentation.orders.components.OrdersSection

@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onNavigateToOrderDetails: (Order) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    OrdersScreenContent(
        state = uiState.value,
        onEvent = { event ->
            when (event) {
                is GoBack -> onNavigateUp()
                is OrdersIntent.OpenOrderDetails -> {
                    onNavigateToOrderDetails(event.order)
                }
                else -> viewModel.processIntent(event)
            }
        }
    )
}

@Composable
private fun OrdersScreenContent(
    state: OrdersUIState,
    onEvent: (OrdersIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(24.dp)
    ) {
        OrdersScreenHeader(
            onBackClicked = { onEvent(GoBack) }
        )

        Spacer(Modifier.height(24.dp))

        if(state.isUserLoggedIn){
            Text(
                text = stringResource(R.string.total_orders, state.totalOrdersCount)
            )

            Spacer(Modifier.height(16.dp))

            OrdersSection(
                orders = state.orders,
                isLoading = state.isNextPageLoading,
                isEndReached = state.isEndReached,
                onRequestNextOrders = {onEvent(OrdersIntent.GetNextOrders)},
                onOrderClicked = { onEvent(OrdersIntent.OpenOrderDetails(order = it)) }
            )
        }else{
            //TODO display the login state here
        }
    }
}