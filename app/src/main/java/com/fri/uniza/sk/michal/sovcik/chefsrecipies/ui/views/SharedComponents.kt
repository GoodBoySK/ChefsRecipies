@file:OptIn(ExperimentalFoundationApi::class)

package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.core.content.FileProvider
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.R
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme
import java.io.File
import java.io.FileOutputStream

fun loadBitMap(context: Context,path:String):Bitmap?
{
    val file = File(context.filesDir, path)
    if (!file.exists())return null

    return BitmapFactory.decodeFile(file.path)
}
fun saveImage(context: Context,path:String, bitmap: Bitmap):String
{
    val file = File(context.filesDir, path)
    val parentDir = file.parentFile

    if (parentDir != null && !parentDir.exists()) {
        parentDir.mkdirs()
    }
    try {

        var outputStream = FileOutputStream(file)
        outputStream.use {
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        }
    }catch (e:Exception)
    {
        Log.e("Image saving","error at saving Image")
    }
    return context.filesDir.path + path
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RecipeCard(recipe: Recipe, modifier: Modifier, onClick: () -> Unit,height: Dp = 250.dp,width:Dp = 250.dp, onRemove: () -> Unit = {}) {
    var expandedDropDown by rememberSaveable {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier
            //  .shadow(3.dp)
            .height(height)
            .width(width)
            .padding(10.dp)
            .combinedClickable(

                onLongClick = {
                    expandedDropDown = true
                },
                onClick = onClick),

    ){
       // Image(painter = , contentDescription = )
        Column {
            Box (
                modifier = Modifier.weight(1f)
            ){
                val bitmap = loadBitMap(LocalContext.current,recipe.name + "/main.png")?.asImageBitmap()
                if (bitmap != null) {
                    Image(bitmap = bitmap,
                    contentDescription = "RecipePicture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize())
                }
            }
            Text(text = recipe.name, fontSize = 20.sp)
            Text(text = recipe.autor, fontStyle = FontStyle.Italic)
        }
        DropdownMenu(expanded = expandedDropDown, onDismissRequest = {expandedDropDown = false }) {
            DropdownMenuItem(text = { Text(text = stringResource(R.string.delete)) }, onClick =
                {
                    onRemove()
                    expandedDropDown = false
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ImageWithChose(context: Context,
                   modifier: Modifier = Modifier,
                    path: String,
                   onImgChose:((newPath:Bitmap?, path:String) -> Unit)? =null,
                   contentDescription: String = "",
                   editable:Boolean, contentScale: ContentScale = ContentScale.Crop)
{
    var boolean by remember {
        mutableStateOf(false)
    }

    var imagePickerActivity = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it != null) {

                val decoder = ImageDecoder.createSource(context.contentResolver,it)
                val bitmap = ImageDecoder.decodeBitmap(decoder)
                saveImage(context,path, bitmap)
                onImgChose?.invoke(ImageDecoder.decodeBitmap(decoder), it.path ?: "")
            }
            onImgChose?.invoke(null, "")
            boolean = false
        }
    )
    var tempUri: Uri? by rememberSaveable {
        mutableStateOf(null)
    }

    var cameraPickerActivity = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = tempUri?.let {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
                if (data != null) {
                    saveImage(context, path, data)
                }
                onImgChose?.invoke(data, context.filesDir.path + path)
            }
            onImgChose?.invoke(null, "")
            boolean = false
        }

    )


    Box(modifier = modifier )
    {
        val image = loadBitMap(LocalContext.current,path)?.asImageBitmap()
        if (image != null){
            Column {
                Image(
                    bitmap = image,
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
                            })
                        DropdownMenuItem(
                            text = { Text(text = stringResource(R.string.take_photo)) },
                            onClick = {
                                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                if (takePictureIntent.resolveActivity(context.packageManager) != null) {
                                    cameraPickerActivity.launch(takePictureIntent)
                                }
                            })

                    }
                }
            }
        }
        else if (editable) {

            Column (modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally){
                Icon(Icons.Filled.Image, contentDescription = "image",modifier = Modifier.size(60.dp,60.dp), tint = Color.Gray)
                Button(onClick = { boolean = true }) {
                    Text(text = stringResource(R.string.add_image))
                }
                DropdownMenu(expanded = boolean, onDismissRequest = { boolean = false }) {
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.from_galery)) }, onClick = {
                        imagePickerActivity.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    })
                    DropdownMenuItem(text = { Text(text = stringResource(R.string.take_photo)) }, onClick = {
                    /*TODO*/
                        val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        if (takePicIntent.resolveActivity(context.packageManager) != null) {

                            val tempFile = File.createTempFile(
                                "JPEG_${System.currentTimeMillis()}}",
                                ".jpg",
                                context.getExternalFilesDir(null)
                                )
                            tempUri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                tempFile
                            )
                            takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri)
                            cameraPickerActivity.launch(takePicIntent)
                        }
                    }
                    )

                }
            }
        }

    }
}

@Composable
fun NumberTextField(number: String, modifier: Modifier = Modifier, enabled: Boolean = true, suffix: @Composable (()->Unit)? = null, prefix: @Composable (()->Unit)? = null, onValueChange:((it:String)->Unit)? = null, textStyle:TextStyle = MaterialTheme.typography.bodyLarge) {
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
fun DecimalTextField(number: String, modifier: Modifier = Modifier, enabled: Boolean = true,
                     suffix: @Composable (()->Unit)? = null, prefix: @Composable (()->Unit)? = null,
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
        ImageWithChose(context = LocalContext.current, path = "" ,editable = true)
    }
}

@Preview
@Composable
private fun CardPreview() {
    ChefsRecipiesTheme {
        RecipeCard(Recipe(1,"American Cookies",DishType.MainDish,30,15,4.25f,"Michalito", ""), Modifier,{})
    }
}