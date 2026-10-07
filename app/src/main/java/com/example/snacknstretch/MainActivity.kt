package com.example.snacknstretch

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snacknstretch.data.ScheduledDataRepository
import com.example.snacknstretch.model.BreakTime
import com.example.snacknstretch.model.MainUiState
import com.example.snacknstretch.model.WorkingTime
import com.example.snacknstretch.ui.composable.NotificationPermissionRequest
import com.example.snacknstretch.ui.composable.SwitchButton
import com.example.snacknstretch.ui.composable.TimeChooser
import com.example.snacknstretch.ui.theme.SnacknStretchTheme
import com.example.snacknstretch.viewmodel.MainViewModel
import com.example.snacknstretch.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnacknStretchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

private val Context.dataStore by preferencesDataStore(name = "settings")

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val repository = remember {
        ScheduledDataRepository(application.dataStore, context)
    }
    val factory = remember {
        MainViewModelFactory(
            application = application,
            repository = repository
        )
    }
    val viewModel: MainViewModel = viewModel(
        factory = factory
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotificationPermissionRequest()
    if (uiState.isLoading) {
        CircularProgressIndicator()
    } else {
        MainContent(
            modifier, viewModel, uiState
        )
    }
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    uiState: MainUiState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.sub_title),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        TimeChooser(
            timeText = stringResource(R.string.work_min),
            list = WorkingTime.entries.map { item -> item.time }.toList(),
            chosenItem = uiState.workingTime.ordinal
        ) { index ->
            viewModel.updateState(
                uiState.copy(
                    workingTime = WorkingTime.entries[index]
                )
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        TimeChooser(
            timeText = stringResource(R.string.break_min),
            list = BreakTime.entries.map { item -> item.time }.toList(),
            chosenItem = uiState.breakTime.ordinal
        ) { index ->
            viewModel.updateState(
                uiState.copy(
                    breakTime = BreakTime.entries[index]
                )
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.constraints),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(10.dp))
        SwitchButton(
            text = stringResource(R.string.battery_not_low),
            isSwitched = uiState.isBatteryLowSwitched
        ) { changed ->
            viewModel.updateState(
                uiState.copy(
                    isBatteryLowSwitched = changed
                )
            )
        }
        SwitchButton(
            text = stringResource(R.string.only_when_charging),
            isSwitched = uiState.isChargingSwitched
        ) { changed ->
            viewModel.updateState(
                uiState.copy(
                    isChargingSwitched = changed
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = uiState.text,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.startWork()
                }
            ) {
                Text(
                    text = stringResource(R.string.schedule_session),
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(20.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.stopWork()
                }
            ) {
                Text(
                    text = stringResource(R.string.cancel_session),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SnacknStretchTheme {
        MainScreen()
    }
}