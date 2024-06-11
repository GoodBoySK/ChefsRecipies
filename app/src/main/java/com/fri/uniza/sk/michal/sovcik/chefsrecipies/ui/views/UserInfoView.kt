package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.R
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.UserInfoViewModel
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.UserInfoViewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun UserInfoView(modifier: Modifier = Modifier, viewModel: UserInfoViewModel)
{
    val pic = if (viewModel.profilePicture.collectAsState().value == null) {
        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.baseline_image_24)
    }
    else {
        viewModel.profilePicture.collectAsState().value
    };
    val focusManager = LocalFocusManager.current
    val localContext = LocalContext.current
    Column {
        Surface (
            shape = RectangleShape,
            shadowElevation = 8.dp,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)

        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box (modifier = Modifier.height(100.dp).width(100.dp).clip(CircleShape)) {
                    ImageWithChose(modifier = Modifier,
                        context = LocalContext.current,
                        onImgChose = {bit,path ->
                            if (bit != null) viewModel.saveProfilePicture(localContext, bit)
                        },
                        editable = true,
                        path = "profile.png"
                    )
                }
                BasicTextField(modifier = Modifier.align(Alignment.CenterHorizontally),
                    value = viewModel.userName.collectAsState().value,
                    onValueChange = {
                        viewModel.editUserName(it)
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        viewModel.onDone()
                        focusManager.clearFocus()
                    })
                    ,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center)

                )

            }
        }
    }


}

@RequiresApi(Build.VERSION_CODES.P)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun UserInfoPreview() {
    val dataStorePreferences: DataStore<Preferences> = remember{
        object : DataStore<Preferences> {
            override val data: Flow<Preferences>
                get() = flowOf()

            override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
                TODO("Not yet implemented")
            }

        }

    }
    val viewModel = viewModel<UserInfoViewModel>(factory = UserInfoViewModelFactory(dataStorePreferences, LocalContext.current))
    ChefsRecipiesTheme {
        UserInfoView(viewModel = viewModel)
    }
}