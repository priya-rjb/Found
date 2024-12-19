package com.ait.finalproject.ui.screen.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageSwitcher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberImagePainter
import com.ait.finalproject.data.CategoryType
import com.ait.finalproject.data.LikeType
import com.ait.finalproject.data.Place
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewPlaceDialog(
    latLng: LatLng,
    onAddPlace: (String, String, LikeType, CategoryType, Date, Int, Boolean, Float, String) -> Unit,
    onDialogClose: () -> Unit = {},
) {
    var placeTitle by remember { mutableStateOf("") }
    var placeText by remember { mutableStateOf("") }
    var placeLiked by remember { mutableStateOf("") }
    var categorySelected by remember { mutableStateOf("") }
    var likeCategory: LikeType by remember {
        mutableStateOf(LikeType.NEUTRAL)
    }
    var category:CategoryType by remember {
        mutableStateOf(CategoryType.ENTERTAINMENT)
    }
    var date by remember { mutableStateOf(Calendar.getInstance().time)}
    var atmosphere by remember { mutableStateOf(5) } // Default rank value
    var isDatePickerVisible by remember { mutableStateOf(false) }
    var recommend by remember { mutableStateOf(false)}
    var ranking by remember { mutableStateOf(5f) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Dialog(onDismissRequest = onDialogClose) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
//                Text(text = "Lat: ${latLng.latitude}")
//                Text(text = "Long: ${latLng.longitude}")
                Text(
                    text = "Found A  Place?",
                    modifier = Modifier
                        .padding(top = 10.dp),
                )

                OutlinedTextField(value = placeTitle,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Place title") },
                    onValueChange = {
                        placeTitle = it
                    }
                )
                OutlinedTextField(value = placeText,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Review") },
                    onValueChange = {
                        placeText = it
                    }
                )

                // New UI for rank input
                Text(text = "Atmosphere: ")
                Slider(
                    value = atmosphere.toFloat(),
                    onValueChange = { atmosphere = it.toInt() },
                    valueRange = 0f..10f,
                    steps = 9
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Date: ", modifier = Modifier.padding(end = 8.dp))
                    OutlinedTextField(
                        value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date),
                        onValueChange = {  },
                        modifier = Modifier.weight(1f),
                        enabled = false
                    )
                    IconButton(
                        onClick = { isDatePickerVisible = true }
                    ) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Select Date")
                    }

                    }

                if (isDatePickerVisible) {
                    DatePickerDialog(
                        onDismissRequest = { isDatePickerVisible = false },
                        onSelectDate = { selectedDate ->
                            date = selectedDate
                            //date = it
                            isDatePickerVisible = false
                        }
                    )

                }

                SpinnerSample(
                    listOf(
                        "Yes", "No", "Neutral"
                    ),
                    preselected = "Did you Like it?",
                    onSelectionChanged = { placeLiked= it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )

                SpinnerSample(
                    listOf(
                        "Museum", "Nature", "Sport", "Entertainment", "Religion", "Food", "Other"
                    ),
                    preselected = "Category",
                    onSelectionChanged = { categorySelected= it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )

                // Checkbox for recommendation
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = recommend,
                        onCheckedChange = { recommend = it }
                    )
                    Text(text = "Recommend to a friend?")
                }


                // Image picker
                Button(onClick = { launcher.launch("image/*") }) {
                    Text(text = "Select Image")
                }
                imageUri?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(top = 10.dp)
                    )
                }

                Button(onClick = {
                    //string to type
                    if (placeLiked == "Yes") {
                        likeCategory = LikeType.YES
                    } else if (placeLiked == "No"){
                        likeCategory = LikeType.NO
                    } else {
                        likeCategory = LikeType.NEUTRAL
                    }


                    if(categorySelected == "Entertainment"){
                        category = CategoryType.ENTERTAINMENT
                    } else if (categorySelected == "Food"){
                        category = CategoryType.FOOD
                    }
                    else if(categorySelected == "Museum"){
                        category = CategoryType.MUSEUM
                    }
                    else if (categorySelected == "Nature")
                    { category= CategoryType.NATURE
                    }
                    else if (categorySelected == "Religion"){
                        category = CategoryType.RELIGION
                    }
                    else if (categorySelected == "Sport"){
                        category = CategoryType.SPORT
                    }
                    else if (categorySelected == "Other"){
                        category = CategoryType.OTHER
                    }

                    //Cal function here:
                    //get ranking

// Invoke onAddPlace with comparison score and rank
                    ranking = calculateRanking(likeCategory, atmosphere, recommend)
                    onAddPlace(placeTitle, placeText, likeCategory, category, date, atmosphere, recommend, ranking, imageUri.toString())
                    onDialogClose()
                }) {
                    Text(text = "Add place")
                }
            }
        }
    }
}

@Composable
fun SpinnerSample(
    list: List<String>,
    preselected: String,
    onSelectionChanged: (myData: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) } // initial value
    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = selected,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(
                Icons.Outlined.ArrowDropDown, null, modifier =
                Modifier.padding(8.dp))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                list.forEach { listEntry ->
                    DropdownMenuItem(
                        onClick = {
                            selected = listEntry
                            expanded = false
                            onSelectionChanged(selected)
                        },
                        text = {
                            Text(
                                text = listEntry,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Start)
                            )
                        },
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onSelectDate: (Date) -> Unit
) {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance().time) } // Initialize with current time
    var datePickerState = rememberDatePickerState(selectedDate.time)

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    onSelectDate(Date(datePickerState.selectedDateMillis!!))
                    onDismissRequest()
                }
            ) {
                Text("Select")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        },
        title = {
            Text("Select Date")
        },
        text = {
            Column(Modifier.padding(16.dp)) {

                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    showModeToggle = true
                )
            }
        }
    )
}

fun calculateRanking(likeCategory: LikeType, atmosphere: Int, recommend: Boolean): Float{
    val baseRank = when (likeCategory) {
        LikeType.YES -> 6.0f
        LikeType.NEUTRAL -> 4.0f
        LikeType.NO -> 2.0f
    }
    val recommendAdjustment = if (recommend) 1f else -1f
    val atmosphereAdjustment = if (atmosphere > 5) baseRank * 0.15f else baseRank * -0.15f
    return baseRank + recommendAdjustment + atmosphereAdjustment
}