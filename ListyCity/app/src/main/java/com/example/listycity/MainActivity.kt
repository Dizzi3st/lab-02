package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities =cityRepository.cities,
                        { cityRepository.addCity(it)},
                        modifier = Modifier.padding(innerPadding),
                        onDeleteCity = { cityRepository.deleteCity(it)}
                    )
                }
            }
        }
    }
}



@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteCity: (String) -> Unit
){
    var newCityName by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val containerColor = if (isPressed) Color.Gray else Color.Blue

    Column (modifier = modifier.fillMaxSize()){
        Row (modifier = Modifier.padding(all = 16.dp)){
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City name")},
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button (

                onClick = {
                    if (newCityName.isNotBlank())
                        onAddCity(newCityName)
                    newCityName = ""
                },
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(containerColor = containerColor)

            ) {
                Text("Add City")

            }
            Button (
                onClick = {

                    if (selectedCity.isNotBlank()) {
                        onDeleteCity(selectedCity)
                        selectedCity = ""
                    }
                },
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(containerColor = containerColor)
            ) {
                Text("Delete City")
            }
        }



        LazyColumn(modifier = modifier.weight(1f),
            verticalArrangement = Arrangement.Bottom){
            items(cities) { city ->
                CityRow(city = city, onItemClick = { city ->
                    if (selectedCity.isNotBlank()) {
                        Modifier.background(Color.White)
                        selectedCity = ""
                    } else {
                        Modifier.background(Color.Gray)
                        selectedCity = city
                    }
                })
            }
        }
    }
}

@Composable
fun CityRow(city: String, onItemClick: (String) -> Unit){ // referred to this article for clickable list items:
    Text(                                                 // https://medium.com/@godlinjosheela/mastering-item-clicks-in-lazy-column-a-jetpack-compose-guide-c5c8affe3cb5
        text = city,
        fontSize = 28.sp,
        modifier = Modifier
            .clickable{onItemClick(city)}
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp)
    )
}

class CityRepository {
    private val _cities = mutableStateListOf(
        "Edmonton","Vancouver","Moscow",
        "Sydney","Berlin","Vienna",
        "Tokyo","Beijing","Osaka",
        "New Delhi"
    )

    val cities: List<String>
        get() = _cities

    fun addCity(city: String){
        _cities.add(city)
    }

    fun deleteCity(city: String){
        _cities.remove(city)
    }
}