package com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.theme.ChefsRecipiesTheme
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.ui.viewmodels.ImportViewModel

@Composable
fun ImportView(modifier: Modifier = Modifier, viewModel: ImportViewModel = androidx.lifecycle.viewmodel.compose.viewModel())
{
    val colorSchee = MaterialTheme.colorScheme
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(colorSchee.primary.copy(alpha = 0.8f),220.dp.value, Offset(50f,250f))
        drawCircle(colorSchee.primary.copy(alpha = 0.8f),220.dp.value, Offset(250f,100f))
        val polygon = RoundedPolygon(radius = 300.dp.value, numVertices = 4, centerX = size.width + 10.dp.value, centerY = 180.dp.value)


        var polygonTriangel = RoundedPolygon(radius = 200.dp.value, numVertices = 3, centerX = size.width/2 + 10.dp.value, centerY = 500.dp.value)
        if (size.width > 1500) polygonTriangel = RoundedPolygon(radius = 300.dp.value, numVertices = 3, centerX = size.width/2 + 10.dp.value, centerY = 400.dp.value)
        rotate(15f){
            drawPath(polygon.toPath().asComposePath(), color = colorSchee.secondary)
            drawPath(polygonTriangel.toPath().asComposePath(), color = colorSchee.tertiary)
        }
        rotate(30f){

        drawRect(colorSchee.secondary,
            Offset(500.dp.value,1400.dp.value),
            size = Size(400f,400f))
        }
        drawCircle(colorSchee.primary,
            220.dp.value,
            Offset(800f,1600f))
        drawCircle(colorSchee.tertiary,
            220.dp.value,
            Offset(100f,800f))

    }
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Import from web",
            fontStyle = MaterialTheme.typography.displayMedium.fontStyle,
            fontSize = MaterialTheme.typography.displayMedium.fontSize,
            lineHeight = MaterialTheme.typography.displayMedium.lineHeight,
            textAlign = TextAlign.Center)
        TextField(
            value = viewModel.fieldText.collectAsState().value,
            onValueChange = {
                            viewModel.textChanged(it)
            },
            colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent),
            placeholder = {

                Text(
                    text = "Paste url of suported website",
                    textAlign = TextAlign.Start,
                    color = Color.Gray
                )
            },


            )
        Button(shape = RectangleShape,
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier,
            enabled = false

        ) {
            Text(text = "Import")
        }
    
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ImportPreview() {
    ChefsRecipiesTheme {
        ImportView()
    }
}