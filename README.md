# MonthAndYearPicker

Month and Year Picker allow user to pick only month and year or only month or only year as required. You will get notified for all action's such as on selection of date, on selection of month and on section of year.

## This is a modified fork.

Original library is here - https://github.com/demogorgorn/MonthAndYearPicker which was forked from https://github.com/premkumarroyal/MonthAndYearPicker 

What's the difference?

This library has been completely re-written in Kotlin along with sample code in Compose UI

# Code

    val today = Calendar.getInstance()
                val builder: MonthPickerDialog.Builder =
                    MonthPickerDialog.Builder(
                        context,
                        object : MonthPickerDialog.OnDateSetListener {
                            override fun onDateSet(selectedMonth: Int, selectedYear: Int) {
                               
                                Toast.makeText(context,"Date set with month$selectedMonth year $selectedYear",Toast.LENGTH_SHORT
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
                        }
                    })
                    .setOnYearChangedListener(object : MonthPickerDialog.OnYearChangedListener {
                        override fun onYearChanged(selectedYear: Int) {
                        }
                    })
                    .build()
                    .show()
                        
## Listeners
  setOnMonthChangedListener(object : MonthPickerDialog.OnMonthChangedListener {
                        override fun onMonthChanged(selectedMonth: Int) {
                        }
                    })
  setOnYearChangedListener(object : MonthPickerDialog.OnYearChangedListener {
                        override fun onYearChanged(selectedYear: Int) {
                        }

## Methods
 Methods | Docs
------------ | -------------
setMaxMonth(maxMonth:Int) |  Maximum month that user can select.
setMinMonth(minMonth:Int) |  Minimum month that user can select.
setMonthRange(minMonth:Int, maxMonth:Int) | set both max and min sections.
setActivatedMonth(activatedMonth: Int) | selected the month when picker opens.
setMaxYear(maxYear:Int) | Maximum year that will be shown in picker.
setMinYear(minYear:Int) | Minimum year that will be shown in picker.
setYearRange(minYear:Int, maxYear:Int) | set both max and min selections.
setActivatedYear(activatedYear: Int) | selected the year when picker opens.
setMonthAndYearRange(minMonth:Int, maxMonth:Int, minYear:Int, maxYear:Int) | set month and year min and max values at once.
showMonthOnly() | Only month selection will be shown.
showYearOnly() | Only year selection will be shown.
setTitle(title:String) | set the title for Month Picker Dialog. By default title will be hidden, it will be visible if value set.
setOnMonthChangedListener(onMonthChangedListener: OnMonthChangedListener?) | Listener for select month
setOnYearChangedListener(onYearChangedListener: OnYearChangedListener?) | Listener for year select year

## Styling

Month and Year picker by default pick the color from theme if you declared colorAccent. If you want to change color's you can override the theme as below.

     <style name="MonthPickerDialogStyle" >
        <item name="monthBgColor">@color/bgColor</item>
        <item name="monthBgSelectedColor">@color/colorAccent</item>
        <item name="monthFontColorSelected">@color/selectionColor</item>
        <item name="monthFontColorNormal">@color/bgColor</item>
        <item name="monthFontColorDisabled">@color/bgColor</item>

        <item name="headerBgColor">@color/colorAccent</item>
        <item name="headerFontColorSelected">#fff</item>
        <item name="headerFontColorNormal">#85FFFFFF</item>
        <item name="headerTitleColor">#fff</item>

        <item name="dialogActionButtonColor">@color/colorAccent</item>
    </style>


## Usage 

Step 1. Add it in your root build.gradle at the end of repositories:

    allprojects {
        repositories {
          ...
          mavenCentral()
        }
    }

Step 2. Add the dependency

    dependencies {
        implementation("com.github.SelvinAndroiT:MonthAndYearPicker:1.0.0")
    }
   
