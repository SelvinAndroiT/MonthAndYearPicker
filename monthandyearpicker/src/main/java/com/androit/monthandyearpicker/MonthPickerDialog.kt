package com.androit.monthandyearpicker

import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
import androidx.appcompat.app.AlertDialog
import java.util.Calendar
import java.util.Locale
import androidx.annotation.IntRange

class MonthPickerDialog private constructor(
    context: Context,
    theme: Int,
    callBack: OnDateSetListener,
    year: Int,
    monthOfYear: Int
) : AlertDialog(context, theme),
    DialogInterface.OnClickListener, OnDateChangedListener {

    private val datePicker: MonthPickerView
    private val _callBack: OnDateSetListener?
    private val view: View?

    /**
     * @param context     The context the dialog is to run in.
     * @param callBack    How the parent is notified that the date is set.
     * @param year        The initial year of the dialog.
     * @param monthOfYear The initial month of the dialog.
     */
    private constructor(
        context: Context,
        callBack: OnDateSetListener,
        year: Int,
        monthOfYear: Int
    ) : this(context, 0, callBack, year, monthOfYear)


    override fun show() {
        if (view != null) {
            if (this.getContext().getResources().getConfiguration().orientation ===
                Configuration.ORIENTATION_LANDSCAPE
            ) {
                val lp = WindowManager.LayoutParams()
                if (window != null) {
                    lp.copyFrom(window!!.attributes)
                    lp.width =
                        (this.context.resources.displayMetrics.widthPixels * 0.94).toInt()
                    lp.height =
                        (this.context.resources.displayMetrics.heightPixels * 0.94).toInt()
                    super.show()
                    // now dialog attached to window so apply the size
                    window!!.setLayout(lp.width, lp.height)
                }

                return
            } else {
                dismiss()
            }
        }
        super.show()
    }

    /**
     * @param context     The context the dialog is to run in.
     * @param theme       the theme to apply to this dialog
     * @param callBack    How the parent is notified that the date is set.
     * @param year        The initial year of the dialog.
     * @param monthOfYear The initial month of the dialog.
     */
    init {
        _callBack = callBack
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater.inflate(R.layout.month_picker_dialog, null)

        setView(view)
        datePicker = view!!.findViewById<View>(R.id.monthPicker) as MonthPickerView
        datePicker.setOnDateListener(object : MonthPickerView.OnDateSet {
            override fun onDateSet() {
                tryNotifyDateSet()
                this@MonthPickerDialog.dismiss()
            }
        })
        datePicker.setOnCancelListener(object : MonthPickerView.OnCancel {
            override fun onCancel() {
                this@MonthPickerDialog.dismiss()
            }
        })

        // to show dialog bigger view in landscape mode we are increasing the
        // height and width of the dialog. If we do that android don't dismiss the dialog after
        // rotation and try to render landscape UI in portrait mode which is not correct.
        // so dismissing the dialog on each time when orientation changes.
        datePicker.setOnConfigurationChanged(object : OnConfigChangeListener {
            override fun onConfigChange() {
                dismiss()
            }
        })
        datePicker.init(year, monthOfYear)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        tryNotifyDateSet()
    }

    override fun onDateChanged(view: DatePicker, year: Int, month: Int, day: Int) {
        datePicker.init(year, month)
    }


    fun tryNotifyDateSet() {
        if (_callBack != null) {
            datePicker.clearFocus()
            _callBack.onDateSet(datePicker._selectedMonth, datePicker._selectedYear)
        }
    }

    protected override fun onStop() {
        super.onStop()
    }


    private fun setMinMonth(minMonth: Int) {
        datePicker.setMinMonth(minMonth)
    }

    private fun setMaxMonth(maxMonth: Int) {
        datePicker.setMaxMonth(maxMonth)
    }

    private fun setLocale(locale: Locale) {
        datePicker.setLocale(locale)
    }

    private fun setMonthFormat(format: String) {
        datePicker.setMonthFormat(format)
    }

    private fun setMonthTitleFormat(format: String) {
        datePicker.setMonthTitleFormat(format)
    }

    private fun setMonthNamesArray(resArrayId: Int) {
        datePicker.setMonthNamesArray(resArrayId)
    }

    fun setMonthSelectedCircleSize(size: Int) {
        datePicker.setMonthSelectedCircleSize(size)
    }

    fun setPositiveText(resId: Int) {
        datePicker.setPositiveText(resId)
    }

    fun setNegativeText(resId: Int) {
        datePicker.setNegativeText(resId)
    }

    private fun setMinYear(minYear: Int) {
        datePicker.setMinYear(minYear)
    }

    private fun setMaxYear(maxYear: Int) {
        datePicker.setMaxYear(maxYear)
    }


    private fun setActivatedMonth(activatedMonth: Int) {
        datePicker.setActivatedMonth(activatedMonth)
    }

    private fun setActivatedYear(activatedMonth: Int) {
        datePicker.setActivatedYear(activatedMonth)
    }

    private fun setMonthPickerTitle(title: String) {
        datePicker.setTitle(title)
    }

    private fun showMonthOnly() {
        datePicker.showMonthOnly()
    }

    private fun showYearOnly() {
        datePicker.showYearOnly()
    }

    private fun setOnMonthChangedListener(onMonthChangedListener: OnMonthChangedListener?) {
        if (onMonthChangedListener != null) {
            datePicker.setOnMonthChangedListener(onMonthChangedListener)
        }
    }

    private fun setOnYearChangedListener(onYearChangedListener: OnYearChangedListener?) {
        if (onYearChangedListener != null) {
            datePicker.setOnYearChangedListener(onYearChangedListener)
        }
    }

    class Builder(
        context: Context,
        callBack: OnDateSetListener,
        year: Int,
        @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) month: Int
    ) {
        private val _context: Context
        private val _callBack: OnDateSetListener
        private var _activatedMonth = 0
        private var _activatedYear = 0
        private var _minMonth = Calendar.JANUARY
        private var _maxMonth = Calendar.DECEMBER
        private var _minYear = 0
        private var _maxYear = 0
        private var monthOnly = false
        private var yearOnly = false
        private var title: String? = null
        private lateinit var monthPickerDialog: MonthPickerDialog
        private var _onYearChanged: OnYearChangedListener? = null
        private var _onMonthChanged: OnMonthChangedListener? = null
        private var _locale: Locale? = null
        private var _monthNamesArrayResId = -1
        private var _monthFormat: String? = null
        private var _monthTitleFormat: String? = null
        private var _monthSelectedCircleSize = 43
        private var _positiveText = -1
        private var _negativeText = -1

        /**
         * Build a Dialog with month and year with given context.
         *
         * @param context  Context: the parent context
         * @param callBack MonthPickerDialog.OnDateSetListener: the listener to call
         * when the user sets the date
         * @param year     the initially selected year
         * @param month    the initially selected month (0-11 for compatibility with
         * [Calendar]Calender.MONTH or Calendar.JANUARY, Calendar.FEBRUARY etc)
         */
        init {
            if (month >= Calendar.JANUARY && month <= Calendar.DECEMBER) {
                this._activatedMonth = month
            } else {
                throw IllegalArgumentException(
                    "Month range should be between 0 " +
                            "(Calender.JANUARY) to 11 (Calendar.DECEMBER)"
                )
            }


            if (year >= 1) {
                this._activatedYear = year
            } else {
                throw IllegalArgumentException("Selected year should be > 1")
            }

            this._context = context
            this._callBack = callBack

            if (year > MonthPickerView._minYear) {
                _minYear = MonthPickerView._minYear
            } else {
                _minYear = year
                MonthPickerView._minYear = year
            }

            if (year > MonthPickerView._maxYear) {
                _maxYear = year
                MonthPickerView._maxYear = year
            } else {
                _maxYear = MonthPickerView._maxYear
            }
        }

        /**
         * Minimum enable month in picker (0-11 for compatibility with Calender.MONTH or
         * Calendar.JANUARY, Calendar.FEBRUARY etc).
         *
         * @param minMonth
         * @return Builder
         */
        fun setMinMonth(
            @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) minMonth: Int
        ): Builder {
            if (minMonth >= Calendar.JANUARY && minMonth <= Calendar.DECEMBER) {
                this._minMonth = minMonth
                return this
            } else {
                throw IllegalArgumentException(
                    "Month range should be between 0 " +
                            "(Calender.JANUARY) to 11 (Calendar.DECEMBER)"
                )
            }
        }

        /**
         * Maximum enabled month in picker (0-11 for compatibility with Calender.MONTH or
         * Calendar.JANUARY, Calendar.FEBRUARY etc).
         *
         * @param maxMonth
         * @return
         */
        fun setMaxMonth(
            @IntRange(
                from = Calendar.JANUARY.toLong(),
                to = Calendar.DECEMBER.toLong()
            ) maxMonth: Int
        ): Builder {
            /* if (maxMonth >= Calendar.JANUARY && maxMonth <= Calendar.DECEMBER) {*/
            this._maxMonth = maxMonth
            return this
            /*} else {
                throw new IllegalArgumentException("Month range should be between 0 " +
                        "(Calender.JANUARY) to 11 (Calendar.DECEMBER)");
            }*/
        }


        /**
         * Starting year in the picker.
         *
         * @param minYear
         * @return Builder
         */
        fun setMinYear(minYear: Int): Builder {
            this._minYear = minYear
            return this
        }

        /**
         * Date format for month name.
         *
         * @param format
         * @return Builder
         */
        fun setMonthFormat(format: String?): Builder {
            this._monthFormat = format
            return this
        }

        /**
         * Date format for month name in the title.
         *
         * @param format
         * @return Builder
         */
        fun setMonthTitleFormat(format: String?): Builder {
            this._monthTitleFormat = format
            return this
        }

        /**
         * Resource array with month names.
         *
         * @param arrayResId
         * @return Builder
         */
        fun setMonthNamesArray(arrayResId: Int): Builder {
            this._monthNamesArrayResId = arrayResId
            return this
        }

        fun setMonthSelectedCircleSize(size: Int): Builder {
            this._monthSelectedCircleSize = size
            return this
        }

        /**
         * Resource array with month names.
         *
         * @param locale
         * @return Builder
         */
        fun setLocale(locale: Locale?): Builder {
            this._locale = locale
            return this
        }

        fun setPositiveText(resId: Int): Builder {
            this._positiveText = resId
            return this
        }

        fun setNegativeText(resId: Int): Builder {
            this._negativeText = resId
            return this
        }

        /**
         * Ending year in the picker.
         *
         * @param maxYear
         * @return Builder
         */
        fun setMaxYear(maxYear: Int): Builder {
            this._maxYear = maxYear
            return this
        }

        /**
         * Initially selected month (0-11 for compatibility with Calender.MONTH or
         * Calendar.JANUARY, Calendar.FEBRUARY etc).
         *
         * @param activatedMonth
         * @return Builder
         */
        fun setActivatedMonth(
            @IntRange(
                from = Calendar.JANUARY.toLong(),
                to = Calendar.DECEMBER.toLong()
            ) activatedMonth: Int
        ): Builder {
            this._activatedMonth = activatedMonth
            return this
        }

        /**
         * Initially selected year (0-11 for compatibility with Calender.MONTH or
         * Calendar.JANUARY, Calendar.FEBRUARY etc).
         *
         * @param activatedYear
         * @return Builder
         */
        fun setActivatedYear(activatedYear: Int): Builder {
            this._activatedYear = activatedYear
            return this
        }


        /**
         * Minimum and Maximum enable month in picker (0-11 for compatibility with Calender.MONTH or
         * Calendar.JANUARY, Calendar.FEBRUARY etc).
         *
         * @param minMonth minimum enabled month.
         * @param maxMonth maximum enabled month.
         * @return Builder
         */
        fun setMonthRange(
            @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) minMonth: Int,
            @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) maxMonth: Int
        ): Builder {
            if (minMonth >= Calendar.JANUARY && minMonth <= Calendar.DECEMBER && maxMonth >= Calendar.JANUARY && maxMonth <= Calendar.DECEMBER) {
                this._minMonth = minMonth
                this._maxMonth = maxMonth
                return this
            } else {
                throw IllegalArgumentException(
                    "Month range should be between 0 " +
                            "(Calender.JANUARY) to 11 (Calendar.DECEMBER)"
                )
            }
        }

        /**
         * Starting and ending year show in picker
         *
         * @param minYear starting year
         * @param maxYear ending year
         * @return
         */
        fun setYearRange(minYear: Int, maxYear: Int): Builder {
            if (minYear <= maxYear) {
                this._minYear = minYear
                this._maxYear = maxYear
                return this
            } else {
                throw IllegalArgumentException("Minimum year should be less then Maximum year")
            }
        }

        /**
         * Set the Minimum, maximum enabled months and starting , ending years.
         *
         * @param minMonth minimum enabled month in picker
         * @param maxMonth maximum enabled month in picker
         * @param minYear  starting year
         * @param maxYear  ending year
         * @return
         */
        fun setMonthAndYearRange(
            @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) minMonth: Int,
            @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) maxMonth: Int,
            minYear: Int, maxYear: Int
        ): Builder {
            if (minMonth >= Calendar.JANUARY && minMonth <= Calendar.DECEMBER && maxMonth >= Calendar.JANUARY && maxMonth <= Calendar.DECEMBER) {
                this._minMonth = minMonth
                this._maxMonth = maxMonth
            } else {
                throw IllegalArgumentException(
                    "Month range should be between 0 " +
                            "(Calender.JANUARY) to 11 (Calendar.DECEMBER)"
                )
            }

            if (minYear <= maxYear) {
                this._minYear = minYear
                this._maxYear = maxYear
            } else {
                throw IllegalArgumentException("Minimum year should be less then Maximum year")
            }
            return this
        }

        /**
         * User can select month only. Year won't be shown to user once user select the month.
         *
         * @return Builder
         */
        fun showMonthOnly(): Builder {
            if (yearOnly) {
                Log.e(
                    TAG, "yearOnly also set to true before. Now setting yearOnly to false" +
                            " monthOnly to true"
                )
            }
            this.yearOnly = false
            this.monthOnly = true
            return this
        }

        /**
         * User can select year only. Month won't be shown to user once user select the month.
         *
         * @return Builder
         */
        fun showYearOnly(): Builder {
            if (monthOnly) {
                Log.e(
                    TAG, "monthOnly also set to true before. Now setting monthOnly to " +
                            "false and yearOnly to true"
                )
            }
            this.monthOnly = false
            this.yearOnly = true
            return this
        }

        /**
         * Set the title to the picker.
         *
         * @param title
         * @return Builder
         */
        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        /**
         * Sets the callback that will be called when user click on any month.
         *
         * @param onMonthChangedListener
         * @return Builder
         */
        fun setOnMonthChangedListener(onMonthChangedListener: OnMonthChangedListener?): Builder {
            this._onMonthChanged = onMonthChangedListener
            return this
        }

        /**
         * Sets the callback that will be called when the user select any year.
         *
         * @param onYearChangedListener
         * @return Builder
         */
        fun setOnYearChangedListener(onYearChangedListener: OnYearChangedListener?): Builder {
            this._onYearChanged = onYearChangedListener
            return this
        }

        fun build(): MonthPickerDialog {
            require(_minMonth <= _maxMonth) {
                "Minimum month should always " +
                        "smaller then maximum month."
            }

            require(_minYear <= _maxYear) {
                "Minimum year should always " +
                        "smaller then maximum year."
            }

            require(!(_activatedMonth < _minMonth || _activatedMonth > _maxMonth)) {
                "Activated month should always " +
                        "in between Minimum and maximum month."
            }

            require(!(_activatedYear < _minYear || _activatedYear > _maxYear)) {
                "Activated year should always " +
                        "in between Minimum year and maximum year."
            }


            monthPickerDialog = MonthPickerDialog(
                _context, _callBack, _activatedYear,
                _activatedMonth
            )
            if (monthOnly) {
                monthPickerDialog.showMonthOnly()
                _minYear = 0
                _maxYear = 0
                _activatedYear = 0
            } else if (yearOnly) {
                monthPickerDialog.showYearOnly()
                _minMonth = 0
                _maxMonth = 0
                _activatedMonth = 0
            }

            if (_locale != null) monthPickerDialog.setLocale(_locale!!)

            if (_monthNamesArrayResId != -1) monthPickerDialog.setMonthNamesArray(
                _monthNamesArrayResId
            )

            if (_monthFormat != null) monthPickerDialog.setMonthFormat(_monthFormat!!)

            monthPickerDialog.setPositiveText(_positiveText)
            monthPickerDialog.setNegativeText(_negativeText)

            monthPickerDialog.setMonthSelectedCircleSize(_monthSelectedCircleSize)

            monthPickerDialog.setMinMonth(_minMonth)
            monthPickerDialog.setMaxMonth(_maxMonth)
            monthPickerDialog.setMinYear(_minYear)
            monthPickerDialog.setMaxYear(_maxYear)
            monthPickerDialog.setActivatedMonth(_activatedMonth)
            monthPickerDialog.setActivatedYear(_activatedYear)

            if (_onMonthChanged != null) {
                monthPickerDialog.setOnMonthChangedListener(_onMonthChanged)
            }

            if (_onYearChanged != null) {
                monthPickerDialog.setOnYearChangedListener(_onYearChanged)
            }

            if (title != null) {
                monthPickerDialog.setMonthPickerTitle(title!!.trim { it <= ' ' })
            }
            return monthPickerDialog
        }

        companion object {
            private val TAG: String = Builder::class.java.name
        }
    }

    /**
     * The callback used to indicate the user is done selecting month.
     */
    interface OnDateSetListener {
        /**
         * @param selectedMonth The month that was set (0-11) for compatibility with [Calendar].
         * @param selectedYear  The year that was set.
         */
        fun onDateSet(selectedMonth: Int, selectedYear: Int)
    }

    /**
     * The callback used to indicate the user click on month
     */
    interface OnMonthChangedListener {
        /**
         * @param selectedMonth The month that was set (0-11) for compatibility
         * with [Calendar].
         */
        fun onMonthChanged(selectedMonth: Int)
    }

    /**
     * The callback used to indicate the user click on year.
     */
    interface OnYearChangedListener {
        /**
         * Called upon a year change.
         *
         * @param year The year that was set.
         */
        fun onYearChanged(year: Int)
    }

    interface OnConfigChangeListener {
        fun onConfigChange()
    }
}