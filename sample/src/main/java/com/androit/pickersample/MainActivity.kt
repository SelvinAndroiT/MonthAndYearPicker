package com.androit.pickersample

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.androit.monthandyearpicker.MonthPickerDialog
import com.androit.pickersample.ui.theme.MonthAndYearPickerTheme
import java.util.Calendar

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonthAndYearPickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Modifier.padding(innerPadding)
                    CenteredButton()
                }
            }
        }
    }
}

@Composable
fun CenteredButton() {
    var choosenYear = 2017
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(verticalArrangement = Arrangement.Center) {

            Button(onClick = {
                val today = Calendar.getInstance()
                val builder: MonthPickerDialog.Builder =
                    MonthPickerDialog.Builder(
                        context,
                        object : MonthPickerDialog.OnDateSetListener {
                            override fun onDateSet(selectedMonth: Int, selectedYear: Int) {
                                Log.d(
                                    "TAG",
                                    "selectedMonth : $selectedMonth selectedYear : $selectedYear"
                                )
                                Toast.makeText(
                                    context,
                                    "Date set with month$selectedMonth year $selectedYear",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        today.get(Calendar.YEAR),
                        today.get(Calendar.MONTH)
                    )
                builder.setActivatedMonth(Calendar.JULY)
                    .setMinYear(1990)
                    .setActivatedYear(2017)
                    .setMaxYear(2030)
                    .setMinMonth(Calendar.FEBRUARY)
                    .setTitle("Select trading month")
                    .setMonthRange(
                        Calendar.FEBRUARY,
                        Calendar.NOVEMBER
                    ) // .setMaxMonth(Calendar.OCTOBER)
                    // .setYearRange(1890, 1890)
                    // .setMonthAndYearRange(Calendar.FEBRUARY, Calendar.OCTOBER, 1890, 1890)
                    //.showMonthOnly()
                    // .showYearOnly()
                    .setOnMonthChangedListener(object : MonthPickerDialog.OnMonthChangedListener {
                        override fun onMonthChanged(selectedMonth: Int) {
                            Log.d("TAG", "Selected month : $selectedMonth")
                            // Toast.makeText(MainActivity.this, " Selected month : " + selectedMonth, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setOnYearChangedListener(object : MonthPickerDialog.OnYearChangedListener {
                        override fun onYearChanged(selectedYear: Int) {
                            Log.d("TAG", "Selected year : $selectedYear")
                            // Toast.makeText(MainActivity.this, " Selected year : " + selectedYear, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .build()
                    .show()

            }) {
                Text("Month Picker")
            }

            Button(onClick = {
                val today = Calendar.getInstance()
                val dialog = DatePickerDialog(
                    context, null, 2017,
                    today.get(Calendar.MONTH), today.get(Calendar.DATE)
                )
                dialog.show()

            }) {
                Text("Date Picker")
            }

            Button(onClick = {
                val builder: MonthPickerDialog.Builder =
                    MonthPickerDialog.Builder(
                        context,
                        object : MonthPickerDialog.OnDateSetListener {
                            override fun onDateSet(selectedMonth: Int, selectedYear: Int) {
                            }
                        },  /* activated number in year */
                        3,
                        5
                    )
                builder.showMonthOnly()
                    .build()
                    .show()

            }) {
                Text("Choose Month")
            }

            Button(onClick = {
                val builder: MonthPickerDialog.Builder =
                    MonthPickerDialog.Builder(
                        context,
                        object : MonthPickerDialog.OnDateSetListener {
                            override fun onDateSet(selectedMonth: Int, selectedYear: Int) {
                                Toast.makeText(
                                    context,
                                    "Date set with year $selectedYear",
                                    Toast.LENGTH_SHORT
                                ).show()
                                choosenYear = selectedYear
                            }
                        },
                        choosenYear,
                        0
                    )
                builder.showYearOnly()
                    .setYearRange(1990, 2030)
                    .build()
                    .show()
            }) {
                Text("Choose Year")
            }

            Button(onClick = {
                val builder: MonthPickerDialog.Builder =
                    MonthPickerDialog.Builder(
                        context,
                        object : MonthPickerDialog.OnDateSetListener {
                            override fun onDateSet(selectedMonth: Int, selectedYear: Int) {
                                Toast.makeText(
                                    context,
                                    "Selected Quantity $selectedYear",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },  /* activated number in year */
                        3,
                        0
                    )
                builder.setActivatedMonth(Calendar.JULY) // .setMaxMonth(Calendar.OCTOBER)
                    //.setMinYear(1990)
                    //.setActivatedYear(3)
                    //.setMinMonth(Calendar.FEBRUARY)
                    //.setMaxYear(2030)
                    .setTitle("Select Quantity") //.setMonthRange(Calendar.FEBRUARY, Calendar.NOVEMBER)
                    .setYearRange(
                        1,
                        15
                    ) // .setMonthAndYearRange(Calendar.FEBRUARY, Calendar.OCTOBER, 1890, 1890)
                    //.showMonthOnly()
                    .showYearOnly()
                    .build()
                    .show()
            }) {
                Text("Choose Quantity")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MonthAndYearPickerTheme {
        CenteredButton()
    }
}