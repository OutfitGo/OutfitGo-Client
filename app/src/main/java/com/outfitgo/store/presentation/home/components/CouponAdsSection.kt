package com.outfitgo.store.presentation.home.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.outfitgo.store.R
import com.outfitgo.store.domain.model.Coupon
import kotlinx.coroutines.delay


@Composable
fun CouponAdsSection(coupons: List<Coupon>) {
    val pagerState = rememberPagerState(pageCount = { coupons.size })
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    if (pagerState.pageCount > 0) {
        LaunchedEffect(pagerState) {
            while (true) {
                delay(4000L)
                val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(12.dp))
        ) { page ->
            val coupon = coupons[page]
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable {
                    clipboardManager.setText(AnnotatedString(coupon.code))
                    Toast.makeText(context, "Copied: ${coupon.code}", Toast.LENGTH_SHORT)
                        .show()
                }) {

                Image(
                    painter = painterResource(R.drawable.ad),
                    contentDescription = "Coupon Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = "Use Code: ${coupon.code}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(end=8.dp)
                )
            }
        }

        Row(
            Modifier
                .height(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}
