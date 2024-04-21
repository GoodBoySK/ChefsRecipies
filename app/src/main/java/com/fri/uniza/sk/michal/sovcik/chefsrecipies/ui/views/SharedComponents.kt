package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.DnsResolver
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.decodeBitmap
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.MainActivity
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCard(recipe: Recipe, modifier: Modifier, onClick: () -> Unit,height: Dp = 250.dp,width:Dp = 250.dp) {


    Card(
        modifier = modifier
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
fun ImageWithChose(contentResolver: ContentResolver,modifier: Modifier = Modifier, bitmap: Bitmap?,onImgChose:(newPath:Bitmap?) -> Unit,contentDescription: String = "",isActive:Boolean,contentScale: ContentScale = ContentScale.FillBounds)
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



    Box(modifier = modifier )
    {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier =  Modifier.fillMaxSize()
            )
        }
        if (bitmap == null && isActive) {

            Column (modifier = Modifier.align(Alignment.Center)){
                Button(onClick = { boolean = true }) {
                    Text(text = "Add Image")
                }
                DropdownMenu(expanded = boolean, onDismissRequest = { boolean = false }) {
                    DropdownMenuItem(text = { Text(text = "From galery...") }, onClick = {
                        imagePickerActivity.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        boolean = false
                    })
                    DropdownMenuItem(text = { Text(text = "Take photo...") }, onClick = {
                    /*TODO*/
                        boolean = false})

                }
            }
        }

    }
}

@Composable
fun NumberTextField(number: Int,modifier: Modifier = Modifier,enabled: Boolean = true,  suffix: @Composable()(()->Unit)? = null,prefix: @Composable()(()->Unit)? = null, onValueChange:((it:Int)->Unit)? = null, textStyle:TextStyle = MaterialTheme.typography.bodyLarge) {
    TextField(
        value = if(number != 0) {
            number.toString()
        }else
        {
            ""
        },
        onValueChange = {
            onValueChange?.let { it1 -> it1(it.replace(",","").replace(".","").toIntOrNull() ?: 0) }
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
fun DecimalTextField(number: Float,modifier: Modifier = Modifier,enabled: Boolean = true,
                     suffix: @Composable()(()->Unit)? = null, prefix: @Composable()(()->Unit)? = null,
                     onValueChange:((it:Float)->Unit)? = null,
                     textStyle:TextStyle = MaterialTheme.typography.bodyLarge) {
    var internalString by remember {
        mutableStateOf(number.toString())
    }
    TextField(
        value = if(number != 0f) {
            internalString
        }else
        {
            ""
        },
        onValueChange = {
            onValueChange?.let { it1 ->
                internalString = it
                var string = it
                if (string.endsWith(",") || string.endsWith(".")) string = string + "0"
                it1(string.toFloatOrNull() ?: 0.0f) }
        },

        textStyle = textStyle,
        enabled = enabled,
        placeholder = {
            Text(
                text = "0.0"
            )
        },

        modifier = modifier.onFocusChanged {
            if (!it.hasFocus) {
                if (internalString.endsWith(",") || internalString.endsWith("."))
                    internalString = internalString + "0"
            }
        },
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
//        ImageWithChose(contentResolver = ContentResolver,bitmap = null, onImgChose = {}, isActive = true)
    }
}

@Preview
@Composable
private fun CardPreview() {
    ChefsRecipiesTheme {
        RecipeCard(Recipe(1,"American Cookies",DishType.MainDish,30,15,4.25f,"Michalito", ""), Modifier,{})
    }
}