package com.outfitgo.store.presentation.mappiker.commponets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.outfitgo.store.core.util.ActionResult

@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    savePlaceState: ActionResult,
    onSaveClicked: () -> Unit
) {
    var isSaveBtnEnabled by remember { mutableStateOf(true) }

    Button(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        enabled = isSaveBtnEnabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.75f)
        ),
        onClick = onSaveClicked
    ) {
        when (savePlaceState) {
            ActionResult.LOADING -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                isSaveBtnEnabled = false
            }

            else -> {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                isSaveBtnEnabled = true
            }
        }
    }
}