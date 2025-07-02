package com.androit.monthandyearpicker


import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import java.util.Calendar
import java.util.Locale

class MonthViewAdapter(private val _context: Context) : BaseAdapter() {
    private var _minMonth = 0
    private var _maxMonth = 0
    private var _activatedMonth = 0
    private var _colors: Map<String, Int>? = null
    private var mOnDaySelectedListener: OnDaySelectedListener? = null
    private var _locale: Locale? = null
    private var _monthFormat = "MMM"
    private var _monthNames: Array<String> = arrayOf()
    private var _monthSelectedCircleSize = 43
    private var _monthTextSize = 0


    override fun getCount(): Int {
        return 1
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View {
        val v: MonthView
        if (convertView != null) {
            v = convertView as MonthView
        } else {
            v = MonthView(_context)
            v.setColors(_colors!!)


            // Set up the new view
            val params = AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT
            )
            v.layoutParams = params
            v.isClickable = true
            v.setOnMonthClickListener(mOnDayClickListener)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            v.setBackgroundDrawable(_context.getDrawable(R.drawable.month_ripplr))
        }
        v.setLocalFormats(_locale, _monthFormat)
        v.setMonthNamesArray(_monthNames)
        v.setMonthParams(_activatedMonth, _minMonth, _maxMonth)
        v.setMonthSelectedCircleSize(_monthSelectedCircleSize)
        v.setMonthTextSize(_monthTextSize)
        v.reuse()
        v.invalidate()
        return v
    }

    private val mOnDayClickListener: MonthView.OnMonthClickListener =
        object : MonthView.OnMonthClickListener {
            override fun onMonthClick(view: MonthView?, day: Int) {
                Log.d("MonthViewAdapter", "onDayClick $day")
                if (isCalendarInRange(day)) {
                    Log.d("MonthViewAdapter", "day not null && Calender in range $day")
                    setSelectedMonth(day)
                    if (mOnDaySelectedListener != null) {
                        mOnDaySelectedListener!!.onDaySelected(this@MonthViewAdapter, day)
                    }
                }
            }
        }

    init {
        setRange()
    }


    fun isCalendarInRange(value: Int): Boolean {
        return value in _minMonth.._maxMonth
    }

    /**
     * Updates the selected day and related parameters.
     *
     * @param month The day to highlight
     */
    fun setSelectedMonth(month: Int) {
        Log.d("MonthViewAdapter", "setSelectedMonth : $month")
        _activatedMonth = month
        notifyDataSetChanged()
    }

    /* set min and max date and years*/
    fun setRange() {
        _minMonth = Calendar.JANUARY
        _maxMonth = Calendar.DECEMBER
        _activatedMonth = Calendar.AUGUST
//        notifyDataSetInvalidated()
    }

    /**
     * Sets the listener to call when the user selects a day.
     *
     * @param listener The listener to call.
     */
    fun setOnDaySelectedListener(listener: OnDaySelectedListener?) {
        mOnDaySelectedListener = listener
    }

    interface OnDaySelectedListener {
        fun onDaySelected(view: MonthViewAdapter?, month: Int)
    }

    fun setMaxMonth(maxMonth: Int) {
        if (maxMonth <= Calendar.DECEMBER && maxMonth >= Calendar.JANUARY) {
            _maxMonth = maxMonth
        } else {
            throw IllegalArgumentException("Month out of range please send months between Calendar.JANUARY, Calendar.DECEMBER")
        }
    }


    fun setMinMonth(minMonth: Int) {
        if (minMonth >= Calendar.JANUARY && minMonth <= Calendar.DECEMBER) {
            _minMonth = minMonth
        } else {
            throw IllegalArgumentException("Month out of range please send months between Calendar.JANUARY, Calendar.DECEMBER")
        }
    }

    fun setActivatedMonth(activatedMonth: Int) {
        if (activatedMonth >= Calendar.JANUARY && activatedMonth <= Calendar.DECEMBER) {
            _activatedMonth = activatedMonth
        } else {
            throw IllegalArgumentException("Month out of range please send months between Calendar.JANUARY, Calendar.DECEMBER")
        }
    }

    fun setColors(map: Map<String, Int>) {
        _colors = map
    }

    fun setLocale(locale: Locale?) {
        _locale = locale
    }

    fun setMonthFormat(format: String) {
        _monthFormat = format
    }

    fun setMonthNamesArray(resArrayId: Int) {
        _monthNames = _context.resources.getStringArray(resArrayId)
    }

    fun setMonthSelectedCircleSize(size: Int) {
        _monthSelectedCircleSize = size
    }

    fun setMonthTextSize(size: Int) {
        _monthTextSize = size
    }
}