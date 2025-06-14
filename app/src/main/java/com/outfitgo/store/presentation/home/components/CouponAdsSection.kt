package com.outfitgo.store.presentation.home.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.outfitgo.store.R
import com.outfitgo.store.domain.model.Coupon
import com.outfitgo.store.presentation.components.shadow
import com.outfitgo.store.presentation.components.shimmerBrush
import kotlinx.coroutines.delay

@Composable
fun CouponAdsSection(isLoading: Boolean, coupons: List<Coupon>) {
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

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isLoading) {
            LoadingState()
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) { page ->
                val coupon = coupons[page]
                CouponCard(coupon = coupon, clipboardManager = clipboardManager, context = context)
            }
        }

        Row(
            Modifier
                .height(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                RowLoadingState()
            } else {
                repeat(pagerState.pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration)
                        MaterialTheme.colorScheme.primary else Color.LightGray
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
}
@Composable
fun CouponCard(
    coupon: Coupon,
    clipboardManager: ClipboardManager,
    context: Context
) {
    var imageVisible by remember { mutableStateOf(false) }

    // Trigger animation on first composition
    LaunchedEffect(Unit) {
        imageVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF704F38), // Your brand color
                        Color(0xFFD7BFAE)  // Soft sand/peach
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                clipboardManager.setText(AnnotatedString(coupon.code))
                Toast.makeText(context, "Copied: ${coupon.code}", Toast.LENGTH_SHORT).show()
            }
            .padding(16.dp)
    ) {
        // Clothes image with animation, rounded border & blur
        AnimatedVisibility(
            visible = imageVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 600)),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_clothes),
                contentDescription = "Clothes Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(120.dp)
            )
        }

        Text(
            text ="And you will get \n${coupon.summary}",
            color = Color(0xFFF2E9E4),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 3,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .width(200.dp)
        )

        Text(
            text = "Use promo code\n${coupon.code}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
    )
}

@Composable
private fun RowLoadingState() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(10.dp)
            .width(32.dp)
            .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
    ) {}
}
