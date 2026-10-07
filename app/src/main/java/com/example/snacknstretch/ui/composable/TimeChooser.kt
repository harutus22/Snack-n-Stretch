package com.example.snacknstretch.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.snacknstretch.R

@Composable
fun TimeChooser(
    modifier: Modifier = Modifier,
    timeText: String,
    list: List<Int>,
    chosenItem: Int,
    onTimeChosen: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = timeText,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(30.dp)
                )
                .clip(RoundedCornerShape(30.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            list.forEachIndexed { index, item ->
                val isSelected = chosenItem == index
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable {
                            onTimeChosen(index)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (isSelected) Color.Cyan else Color.White
                    ),
                    shape = RectangleShape,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AnimatedVisibility(
                            visible = isSelected,
                            enter = fadeIn(),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Checked",
                                tint = Color.Black,
                                modifier = Modifier
                                    .size(16.dp)
                            )
                        }
                        Text(
                            text = item.toString(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                        )
                    }
                }
                VerticalDivider(
                    thickness = 2.dp,
                    color = Color.Gray
                )
            }
        }
        Spacer(Modifier.height(5.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TimeChooserPreview() {
    TimeChooser(
        timeText = stringResource(R.string.work_min),
        chosenItem = 0,
        list = listOf(15, 30, 45, 60),
    ) { }
}