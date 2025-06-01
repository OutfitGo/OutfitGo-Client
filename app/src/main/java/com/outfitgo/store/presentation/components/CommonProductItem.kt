package com.outfitgo.store.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.outfitgo.store.R
import com.outfitgo.store.core.util.CurrencyExchange
import com.outfitgo.store.core.util.toCurrency
import com.outfitgo.store.domain.model.product.CommonProduct


@Composable
fun CommonProductItem(
    product: CommonProduct,
    onProductClicked: (CommonProduct) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
            .background(color = MaterialTheme.colorScheme.secondary)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onProductClicked(product) })
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .fillMaxWidth()
                .height(150.dp)
                .shadow(
                    color = Color(0xFFC2C1C1),

                    blurRadius = 8.dp
                )
                .clip(RoundedCornerShape(8.dp)),
            model = product.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            placeholder = painterResource(R.drawable.product_img_placeholder)
        )

        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = product.name,
            maxLines = 2,
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = product.type,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 12.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        //TODO Handle currency here
        Text(
//            text = stringResource(R.string.le, product.price.toCurrency()),
            text = "${product.price.toCurrency()} ${CurrencyExchange.currentCurrencyUnit}",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
    }
}