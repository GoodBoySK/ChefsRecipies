package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.R
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCard(recipe: Recipe, modifier: Modifier, onClick: () -> Unit,height: Dp = 250.dp,width:Dp = 250.dp) {


    Card(
        modifier = modifier
            .shadow(3.dp)
            .height(height)
            .width(width)
            .padding(10.dp),
        onClick = onClick
    ){
       // Image(painter = , contentDescription = )
        Column (


        ){
            Box (
                modifier = Modifier.weight(1f)

            ){
                recipe.bitmap?.let {
                    Image(bitmap = it.asImageBitmap(),
                        contentDescription = "RecipePicture",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }
            Text(text = recipe.name, fontSize = 20.sp)
            Text(text = recipe.autor, fontStyle = FontStyle.Italic)
        }

    }
}
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ImageWithChose(contentResolver: ContentResolver,
                   modifier: Modifier = Modifier,
                   bitmap: Bitmap?,
                   onImgChose:(newPath:Bitmap?) -> Unit,
                   contentDescription: String = "",
                   editable:Boolean, contentScale: ContentScale = ContentScale.Crop)
{
    var boolean by rememberSaveable {
        mutableStateOf(false)
    }

    var imagePickerActivity = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it != null) {

                val decoder = ImageDecoder.createSource(contentResolver,it)
                onImgChose(ImageDecoder.decodeBitmap(decoder))
            }
            onImgChose(null)
        }
    )
    var tempUri: Uri? = null
    var cameraPickerActivity = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {
            if (it) {

                val decoder = tempUri?.let { it1 ->
                    ImageDecoder.createSource(contentResolver,
                        it1
                    )
                }
                onImgChose(decoder?.let { it1 -> ImageDecoder.decodeBitmap(it1) })
            }
            onImgChose(null)
        }
    )


    Box(modifier = modifier )
    {
        bitmap?.let {
            Column {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { boolean = true }
                )
                if (editable) {
                    DropdownMenu(expanded = boolean, onDismissRequest = { boolean = false }) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.from_galery)) },
                            onClick = {
                                imagePickerActivity.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                                boolean = false
                            })
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.take_photo)) },
                            onClick = {
                                /*TODO*/
                                //cameraPickerActivity.launch(tempUri)
                                boolean = false
                            })

                    }
                }
            }
        }
        if (bitmap == null && editable) {

            Column (modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally){
                Icon(Icons.Filled.Image, contentDescription = "image",modifier = Modifier.size(60.dp,60.dp), tint = Color.Gray)
                Button(onClick = { boolean = true }) {
                    Text(text = stringResource(R.string.add_image))
                }
                DropdownMenu(expanded = boolean, onDismissRequest = { boolean = false }) {
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.from_galery)) }, onClick = {
                        imagePickerActivity.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        boolean = false
                    })
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.take_photo)) }, onClick = {
                    /*TODO*/
                        //cameraPickerActivity.launch(tempUri)
                        boolean = false})

                }
            }
        }

    }
}

@Composable
fun NumberTextField(number: String,modifier: Modifier = Modifier,enabled: Boolean = true,  suffix: @Composable()(()->Unit)? = null,prefix: @Composable()(()->Unit)? = null, onValueChange:((it:String)->Unit)? = null, textStyle:TextStyle = MaterialTheme.typography.bodyLarge) {
    TextField(
        value = number,
        onValueChange = {
            onValueChange?.let { it1 -> it1(it.replace(",","").replace(".","")) }
                        },

        textStyle = textStyle,
        enabled = enabled,
        placeholder = {
            Text(
                text = "0"
            )
        },

        modifier = modifier,
        suffix = suffix,
        prefix = prefix,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, autoCorrect = true),

    )

}


@Composable
fun DecimalTextField(number: String,modifier: Modifier = Modifier,enabled: Boolean = true,
                     suffix: @Composable()(()->Unit)? = null, prefix: @Composable()(()->Unit)? = null,
                     onValueChange:((it:String)->Unit)? = null,
                     textStyle:TextStyle = MaterialTheme.typography.bodyLarge) {
    TextField(
        value = number,
        onValueChange = {
            onValueChange?.let { it1 ->
                it1(it) }
        },

        textStyle = textStyle,
        enabled = enabled,
        placeholder = {
            Text(
                text = "0.0"
            )
        },

        modifier = modifier,
        suffix = suffix,
        prefix = prefix,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )

}


@Preview(showBackground = true)
@Composable
private fun ImagePreview() {
    ChefsRecipiesTheme {
        ImageWithChose(contentResolver = LocalContext.current.contentResolver,bitmap = null, onImgChose = {}, editable = true)
    }
}

@Preview
@Composable
private fun CardPreview() {
    ChefsRecipiesTheme {
        RecipeCard(Recipe(1,"American Cookies",DishType.MainDish,30,15,4.25f,"Michalito", ""), Modifier,{})
    }
}