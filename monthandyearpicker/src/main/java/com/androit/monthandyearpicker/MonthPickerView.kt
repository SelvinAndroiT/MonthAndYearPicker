package com.androit.monthandyearpicker


import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import java.util.Calendar
import java.util.Locale

open class MonthPickerView(var _context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(_context, attrs, defStyleAttr) {
    var _yearView: YearPickerView
    var _monthList: ListView
    private var _monthViewAdapter: MonthViewAdapter
    var _month: TextView
    var _year: TextView
    var _title: TextView
    var _headerFontColorSelected: Int
    var _headerFontColorNormal: Int
    var _showMonthOnly: Boolean = false
    var _selectedMonth: Int = 0
    var _selectedYear: Int = 0
    var _monthNamesArrayResId: Int = -1

    var _titleMonthFormat: String = "MMM"
    var _locale: Locale
    var _monthSelectedCircleSize: Int = 43
    var _onYearChanged: MonthPickerDialog.OnYearChangedListener? = null
    var _onMonthChanged: MonthPickerDialog.OnMonthChangedListener? = null
    var _onDateSet: OnDateSet? = null
    var _onCancel: OnCancel? = null

    var _positiveText: Int = -1
    var _negativeText: Int = -1

    var ok: TextView
    var cancel: TextView

    private val _monthTextSize: Int

    /*private static final int[] ATTRS_TEXT_COLOR = new int[] {
            com.android.internal.R.attr.textColor};
    private static final int[] ATTRS_DISABLED_ALPHA = new int[] {
            com.android.internal.R.attr.disabledAlpha};*/
    constructor(context: Context) : this(context, null) {
        _context = context
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        _context = context
    }

    fun init(year: Int, month: Int) {
        this._selectedYear = year
        this._selectedMonth = month
    }

    fun setMaxMonth(maxMonth: Int) {
        if (maxMonth <= Calendar.DECEMBER && maxMonth >= Calendar.JANUARY) {
            _monthViewAdapter.setMaxMonth(maxMonth)
        } else {
            throw IllegalArgumentException(
                "Month out of range please send months between " +
                        "Calendar.JANUARY, Calendar.DECEMBER"
            )
        }
    }


    fun setMinMonth(minMonth: Int) {
        if (minMonth >= Calendar.JANUARY && minMonth <= Calendar.DECEMBER) {
            _monthViewAdapter.setMinMonth(minMonth)
        } else {
            throw IllegalArgumentException(
                "Month out of range please send months between" +
                        " Calendar.JANUARY, Calendar.DECEMBER"
            )
        }
    }

    fun setLocale(locale: Locale) {
        _locale = locale
        _monthViewAdapter.setLocale(locale)
    }

    fun setMonthFormat(format: String?) {
        _monthViewAdapter.setMonthFormat(format!!)
    }

    fun setMonthTitleFormat(format: String) {
        _titleMonthFormat = format
    }

    fun setMonthNamesArray(resArrayId: Int) {
        _monthNamesArrayResId = resArrayId
        _monthViewAdapter.setMonthNamesArray(resArrayId)
    }

    fun setMonthSelectedCircleSize(size: Int) {
        _monthSelectedCircleSize = size
        _monthViewAdapter.setMonthSelectedCircleSize(size)
    }

    fun setPositiveText(resId: Int) {
        _positiveText = resId

        if (resId != -1) ok.setText(resId)
    }

    fun setNegativeText(resId: Int) {
        _negativeText = resId

        if (resId != -1) cancel.setText(resId)
    }

    fun setMinYear(minYear: Int) {
        _yearView.setMinYear(minYear)
    }

    fun setMaxYear(maxYear: Int) {
        _yearView.setMaxYear(maxYear)
    }

    fun showMonthOnly() {
        _showMonthOnly = true
        _year.visibility = GONE
    }

    fun showYearOnly() {
        _monthList.visibility = GONE
        _yearView.visibility = VISIBLE

        _month.visibility = GONE
        _year.setTextColor(_headerFontColorSelected)
    }

    fun setActivatedMonth(activatedMonth: Int) {
        if (activatedMonth >= Calendar.JANUARY && activatedMonth <= Calendar.DECEMBER) {
            _monthViewAdapter.setActivatedMonth(activatedMonth)
            _month.text = _context.resources.getStringArray(R.array.months)[activatedMonth]
        } else {
            throw IllegalArgumentException("Month out of range please send months between Calendar.JANUARY, Calendar.DECEMBER")
        }
    }

    fun setActivatedYear(activatedYear: Int) {
        _yearView.setActivatedYear(activatedYear)
        _year.text = activatedYear.toString()
    }

    fun setMonthRange(minMonth: Int, maxMonth: Int) {
        if (minMonth < maxMonth) {
            setMinMonth(minMonth)
            setMaxYear(maxMonth)
        } else {
            throw IllegalArgumentException("maximum month is less then minimum month")
        }
    }

    fun setYearRange(minYear: Int, maxYear: Int) {
        if (minYear < maxYear) {
            setMinYear(minYear)
            setMaxYear(maxYear)
        } else {
            throw IllegalArgumentException("maximum year is less then minimum year")
        }
    }

    fun setMonthYearRange(minMonth: Int, maxMonth: Int, minYear: Int, maxYear: Int) {
        setMonthRange(minMonth, maxMonth)
        setYearRange(minYear, maxYear)
    }

    fun setTitle(dialogTitle: String?) {
        if (dialogTitle != null && dialogTitle.trim { it <= ' ' }.length > 0) {
            _title.text = dialogTitle
            _title.visibility = VISIBLE
        } else {
            _title.visibility = GONE
        }
    }

    fun setOnMonthChangedListener(onMonthChangedListener: MonthPickerDialog.OnMonthChangedListener?) {
        if (onMonthChangedListener != null) {
            this._onMonthChanged = onMonthChangedListener
        }
    }

    fun setOnYearChangedListener(onYearChangedListener: MonthPickerDialog.OnYearChangedListener?) {
        if (onYearChangedListener != null) {
            this._onYearChanged = onYearChangedListener
        }
    }

    fun setOnDateListener(onDateSet: OnDateSet?) {
        this._onDateSet = onDateSet
    }

    fun setOnCancelListener(onCancel: OnCancel?) {
        this._onCancel = onCancel
    }


    interface OnDateSet {
        fun onDateSet()
    }

    interface OnCancel {
        fun onCancel()
    }

    var configChangeListener: MonthPickerDialog.OnConfigChangeListener? = null

    init {
        val mInflater = _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mInflater.inflate(R.layout.month_picker_view, this)

        val a = _context.obtainStyledAttributes(
            attrs, R.styleable.monthPickerDialog,
            defStyleAttr, 0
        )
        var headerBgColor = a.getColor(R.styleable.monthPickerDialog_headerBgColor, 0)
        _headerFontColorNormal = a.getColor(R.styleable.monthPickerDialog_headerFontColorNormal, 0)
        _headerFontColorSelected =
            a.getColor(R.styleable.monthPickerDialog_headerFontColorSelected, 0)
        var monthBgColor = a.getColor(R.styleable.monthPickerDialog_monthBgColor, 0)
        var monthBgSelectedColor = a.getColor(R.styleable.monthPickerDialog_monthBgSelectedColor, 0)
        var monthFontColorNormal = a.getColor(R.styleable.monthPickerDialog_monthFontColorNormal, 0)
        var monthFontColorSelected =
            a.getColor(R.styleable.monthPickerDialog_monthFontColorSelected, 0)
        var monthFontColorDisabled =
            a.getColor(R.styleable.monthPickerDialog_monthFontColorDisabled, 0)
        var headerTitleColor = a.getColor(R.styleable.monthPickerDialog_headerTitleColor, 0)
        val actionButtonColor = a.getColor(R.styleable.monthPickerDialog_dialogActionButtonColor, 0)
        _locale = DateHelper.getCurrentLocale(_context)
        _monthTextSize = a.getDimensionPixelSize(
            R.styleable.monthPickerDialog_monthTextSize, TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                16f, _context.resources.displayMetrics
            ).toInt()
        )



        if (monthFontColorNormal == 0) {
            monthFontColorNormal = resources.getColor(R.color.fontBlackEnable)


            /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                monthFontColorNormal = android.R.attr.textColor;
            } else {
                monthFontColorNormal = getResources().getIdentifier("textColor", "attr", null);
            }
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(monthFontColorNormal, outValue, true);
            int color = outValue.data;
            monthFontColorNormal = color;*/


            /*monthFontColorNormal = context.getTheme().resolveAttribute(
                    android.R.attr.textColorPrimary, outValue, true) ? outValue.data : getResources().getColor(R.color.fontBlackEnable);*/
        }

        if (monthFontColorSelected == 0) {
            monthFontColorSelected = resources.getColor(R.color.fontWhiteEnable)
        }

        if (monthFontColorDisabled == 0) {
            monthFontColorDisabled = resources.getColor(R.color.fontBlackDisable)
        }
        if (_headerFontColorNormal == 0) {
            _headerFontColorNormal = resources.getColor(R.color.fontWhiteDisable)
        }
        if (_headerFontColorSelected == 0) {
            _headerFontColorSelected = resources.getColor(R.color.fontWhiteEnable)
        }
        if (headerTitleColor == 0) {
            headerTitleColor = resources.getColor(R.color.fontWhiteEnable)
        }
        if (monthBgColor == 0) {
            monthBgColor = resources.getColor(R.color.fontWhiteEnable)
        }

        if (headerBgColor == 0) {
            headerBgColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                android.R.attr.colorAccent
            } else {
                //Get colorAccent defined for AppCompat
                _context.resources.getIdentifier(
                    "colorAccent",
                    "attr", _context.packageName
                )
            }
            val outValue = TypedValue()
            _context.theme.resolveAttribute(headerBgColor, outValue, true)
            headerBgColor = outValue.data
        }

        if (monthBgSelectedColor == 0) {
            monthBgSelectedColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                android.R.attr.colorAccent
            } else {
                //Get colorAccent defined for AppCompat
                _context.resources.getIdentifier(
                    "colorAccent",
                    "attr", _context.packageName
                )
            }
            val outValue = TypedValue()
            _context.theme.resolveAttribute(monthBgSelectedColor, outValue, true)
            monthBgSelectedColor = outValue.data
        }

        val map = mutableMapOf<String, Int>()
        if (monthBgColor != 0) map["monthBgColor"] = monthBgColor
        if (monthBgSelectedColor != 0) map["monthBgSelectedColor"] = monthBgSelectedColor
        if (monthFontColorNormal != 0) map["monthFontColorNormal"] = monthFontColorNormal
        if (monthFontColorSelected != 0) map["monthFontColorSelected"] = monthFontColorSelected
        if (monthFontColorDisabled != 0) map["monthFontColorDisabled"] = monthFontColorDisabled

        a.recycle()

        _monthList = findViewById<View>(R.id.listview) as ListView
        _yearView = findViewById<View>(R.id.yearView) as YearPickerView
        _month = findViewById<View>(R.id.month) as TextView
        _year = findViewById<View>(R.id.year) as TextView
        _title = findViewById<View>(R.id.title) as TextView
        val _pickerBg = findViewById<View>(R.id.picker_view) as RelativeLayout
        val _header = findViewById<View>(R.id.header) as LinearLayout
        val _actionBtnLay = findViewById<View>(R.id.action_btn_lay) as RelativeLayout
        ok = findViewById<View>(R.id.ok_action) as TextView
        cancel = findViewById<View>(R.id.cancel_action) as TextView


        if (actionButtonColor != 0) {
            ok.setTextColor(actionButtonColor)
            cancel.setTextColor(actionButtonColor)
        } else {
            ok.setTextColor(headerBgColor)
            cancel.setTextColor(headerBgColor)
        }

        if (_headerFontColorSelected != 0) _month.setTextColor(_headerFontColorSelected)
        if (_headerFontColorNormal != 0) _year.setTextColor(_headerFontColorNormal)
        if (headerTitleColor != 0) _title.setTextColor(headerTitleColor)
        if (headerBgColor != 0) _header.setBackgroundColor(headerBgColor)
        if (monthBgColor != 0) _pickerBg.setBackgroundColor(monthBgColor)
        if (monthBgColor != 0) _actionBtnLay.setBackgroundColor(monthBgColor)

        ok.setOnClickListener { _onDateSet!!.onDateSet() }
        cancel.setOnClickListener { _onCancel!!.onCancel() }
        _monthViewAdapter = MonthViewAdapter(_context)
        _monthViewAdapter.setColors(map.toMap())
        _monthViewAdapter.setLocale(_locale)
        _monthViewAdapter.setMonthSelectedCircleSize(_monthSelectedCircleSize)
        _monthViewAdapter.setMonthTextSize(_monthTextSize)

        _monthViewAdapter.setOnDaySelectedListener(object : MonthViewAdapter.OnDaySelectedListener {
            override fun onDaySelected(view: MonthViewAdapter?, selectedMonth: Int) {
                Log.d(
                    "----------------",
                    "MonthPickerDialogStyle selected month = $selectedMonth"
                )
                this@MonthPickerView._selectedMonth = selectedMonth
                if (_monthNamesArrayResId != -1) _month.text =
                    _context.resources.getStringArray(_monthNamesArrayResId)[selectedMonth]
                else _month.setText(
                    DateHelper.getMonthName(
                        selectedMonth,
                        _titleMonthFormat,
                        _locale
                    )
                )

                if (!_showMonthOnly) {
                    _monthList.visibility = GONE
                    _yearView.visibility = VISIBLE
                    _month.setTextColor(_headerFontColorNormal)
                    _year.setTextColor(_headerFontColorSelected)
                }
                _onMonthChanged?.onMonthChanged(selectedMonth)
            }
        })
        _monthList.adapter = _monthViewAdapter

        _yearView.setRange(_minYear, _maxYear)
        _yearView.setColors(map.toMap())
        _yearView.setYear(Calendar.getInstance()[Calendar.YEAR])
        _yearView.setOnYearSelectedListener(object : YearPickerView.OnYearSelectedListener {
            override fun onYearChanged(view: YearPickerView?, selectedYear: Int) {
                Log.d("----------------", "selected year = $selectedYear")
                this@MonthPickerView._selectedYear = selectedYear
                _year.text = "" + selectedYear
                _year.setTextColor(_headerFontColorSelected)
                _month.setTextColor(_headerFontColorNormal)
                _onYearChanged?.onYearChanged(selectedYear)
            }
        })
        _month.setOnClickListener {
            if (_monthList.visibility == GONE) {
                _yearView.visibility = GONE
                _monthList.visibility = VISIBLE
                _year.setTextColor(_headerFontColorNormal)
                _month.setTextColor(_headerFontColorSelected)
            }
        }
        _year.setOnClickListener {
            if (_yearView.visibility === GONE) {
                _monthList.visibility = GONE
                _yearView.visibility = VISIBLE
                _year.setTextColor(_headerFontColorSelected)
                _month.setTextColor(_headerFontColorNormal)
            }
        }
    }

    fun setOnConfigurationChanged(configChangeListener: MonthPickerDialog.OnConfigChangeListener?) {
        this.configChangeListener = configChangeListener
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        configChangeListener?.onConfigChange()
        super.onConfigurationChanged(newConfig)
    }

    companion object {
        var _minYear: Int = 1900
        var _maxYear: Int = Calendar.getInstance()[Calendar.YEAR]
    }
}
