package com.example.snacknstretch.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.snacknstretch.R

@Composable
fun SwitchButton(
    modifier: Modifier = Modifier,
    text: String,
    isSwitched: Boolean,
    onSwitchClick: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text)
        Switch(
            checked = isSwitched,
            onCheckedChange = { onSwitchClick(it) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SwitchButtonPreview() {
    SwitchButton(
        text = stringResource(R.string.battery_not_low),
        isSwitched = false
    ) { }
}