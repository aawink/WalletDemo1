package com.etrade.eo.core.util


import com.google.common.base.Strings
import org.slf4j.LoggerFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Utility class that is used to format different kind of date representation to strings.
 */
object DateFormattingUtils {

    private val DEFAULT_TIME_ZONE = TimeZone.getTimeZone("EST5EDT")

    private val DATE_TEMPLATE = "yyyy-MM-dd"
    private val DISPLAYED_DATE_TEMPLATE = "MM/dd/yyyy"
    private val LONG_MONTH_YEAR_TEMPLATE = "MMMM yyyy"
    private val EXPIRATION_DATE_FORMAT = "MMM-dd-yy"
    private val DATE_TIME_24_HOURS_PATTERN = "MM/dd/yyyy HH:mm:ss"

    private val LOGGER = LoggerFactory.getLogger(DateFormattingUtils::class.java)

    private val US_DATE_FORMAT = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return getDateFormat(DEFAULT_TIME_ZONE)
        }
    }

    private val US_TO_LOCAL_DATE_FORMAT = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return getDateFormat(TimeZone.getDefault())
        }
    }

    private val DATE_TIME_FORMAT = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            //06/23/2016 11:37:29 AM (hours are 1-12)
            val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US)
            dateFormat.timeZone = DEFAULT_TIME_ZONE
            return dateFormat
        }
    }

    private val DATE_TIME_FORMAT_24HR = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            //06/23/2016 13:37:29 (hours are 0-23)
            val dateFormat = SimpleDateFormat(DATE_TIME_24_HOURS_PATTERN, Locale.US)
            dateFormat.timeZone = DEFAULT_TIME_ZONE
            return dateFormat
        }
    }

    private val FULL_DATE_TIME = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            //January 18, 2017 10:21 AM ET
            val dateFormat = SimpleDateFormat("MMMM dd, yyyy hh:mm a z", Locale.US)
            dateFormat.timeZone = DEFAULT_TIME_ZONE
            return dateFormat
        }
    }

    private val DISPLAYED_DATE_FORMAT = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            val dateFormat = SimpleDateFormat(DISPLAYED_DATE_TEMPLATE, Locale.US)
            dateFormat.timeZone = DEFAULT_TIME_ZONE
            return dateFormat
        }
    }

    private val DEVICE_TIMEZONE_DATE_FORMAT = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            val dateFormat = SimpleDateFormat(DISPLAYED_DATE_TEMPLATE, Locale.US)
            dateFormat.timeZone = TimeZone.getDefault()
            return dateFormat
        }
    }

    private val LOCALE_INDEPENDENT_FORMAT = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
            dateFormat.timeZone = DEFAULT_TIME_ZONE
            return dateFormat
        }
    }

    private val LOCAL_FORMAT = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return SimpleDateFormat("MMMM dd, yyyy", Locale.US)
        }
    }

    private val LOCALE_INDEPENDENT_DATE_WITH_DASHES = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            val dateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US)
            dateFormat.timeZone = DEFAULT_TIME_ZONE
            return dateFormat
        }

    }

    private val LOCALE_INDEPENDENT_DATE_WITH_SLASHES = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            dateFormat.timeZone = DEFAULT_TIME_ZONE
            return dateFormat
        }

    }

    private val DEVICE_TIMEZONE_DATE_WITH_SLASHES = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            dateFormat.timeZone = TimeZone.getDefault()
            return dateFormat
        }

    }

    private val LOCALE_INDEPENDENT_SHORT_DATE_WITH_SLASHES = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            val dateFormat = SimpleDateFormat("MM/dd/yy", Locale.US)
            dateFormat.timeZone = DEFAULT_TIME_ZONE
            return dateFormat
        }

    }

    private val LOCALE_INDEPENDENT_FORMAT_WITH_SHORT_MONTH_NAME = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            dateFormat.timeZone = DEFAULT_TIME_ZONE
            return dateFormat
        }

    }

    private val DEVICE_TIMEZONE_FORMAT_WITH_SHORT_MONTH_NAME = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
            dateFormat.timeZone = TimeZone.getDefault()
            return dateFormat
        }

    }

    private val LOCALE_INDEPENDENT_FORMAT_EXPIRATION = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            return SimpleDateFormat(EXPIRATION_DATE_FORMAT, Locale.US)
        }
    }

    private val LOCALE_INDEPENDENT_FORMAT_EXPIRATION_UTC = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            val dateFormat = SimpleDateFormat(EXPIRATION_DATE_FORMAT, Locale.US)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            return dateFormat
        }
    }

    private val LONG_MONTH_YEAR_FORMAT = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            val dateFormat = SimpleDateFormat(LONG_MONTH_YEAR_TEMPLATE, Locale.US)
            dateFormat.timeZone = DEFAULT_TIME_ZONE
            return dateFormat
        }
    }

    private val LOCALE_INDEPENDENT_TIME_FORMAT = object : ThreadLocal<SimpleDateFormat>() {
        override fun initialValue(): SimpleDateFormat {
            val dateFormat = SimpleDateFormat("kk:mm:ss", Locale.US)
            dateFormat.timeZone = DEFAULT_TIME_ZONE
            return dateFormat
        }
    }

    private val usShortDateFormatter: SimpleDateFormat
        get() = US_DATE_FORMAT.get()

    private val usToLocalDateFormat: SimpleDateFormat
        get() = US_TO_LOCAL_DATE_FORMAT.get()

    private val localeIndependentFormatter: SimpleDateFormat
        get() = LOCALE_INDEPENDENT_FORMAT.get()

    private val localFullDateFormatter: SimpleDateFormat
        get() = LOCAL_FORMAT.get()

    private val fullDateTimeFormatter: SimpleDateFormat
        get() = FULL_DATE_TIME.get()

    private val localeIndependentTimeFormatter: SimpleDateFormat
        get() = LOCALE_INDEPENDENT_TIME_FORMAT.get()

    private fun getDateTimeFormatter(amPmFormat: Boolean): SimpleDateFormat {
        return if (amPmFormat) DATE_TIME_FORMAT.get() else DATE_TIME_FORMAT_24HR.get()
    }

    /**
     * @param timeZone timeZone of the required dateFormat, this param must not be null
     *
     * @return a SimpleDateFormat instance with the default date template ("yyyy-MM-dd"),
     * US Locale and the given timeZone.
     */
    @JvmStatic
    fun getDateFormat(timeZone: TimeZone): SimpleDateFormat {
        val dateFormat = SimpleDateFormat(DATE_TEMPLATE, Locale.US)
        dateFormat.timeZone = timeZone
        return dateFormat
    }

    /**
     * @param dateTimeString date string formatted like this: mm/dd/yyyy hh:mm:ss  (where hours are from 00 to 23)
     * or mm/dd/yyyy hh:mm:ss am/pm (where hours are from 01 to 12), this param must not be null
     * @return timestamp of the parsed date or null if parsing fails
     */
    @JvmStatic
    fun parseDateFromString(dateTimeString: String): Long? {
        var time: Long? = null
        try {
            val isAmPmFormat = dateTimeString
                    .map { it -> it.toLowerCase() }
                    .contains('m')
            val parsed = getDateTimeFormatter(isAmPmFormat).parse(dateTimeString)
            time = parsed.time
        } catch (e: ParseException) {
            LOGGER.error(e.message, e)
        }
        return time
    }

    /**
     * @param usShortDate a date representation in the form of "yyyy-MM-dd", this param must not be null
     *
     * @return the date formatted to "MMMM dd, yyyy" or null in case of error
     */
    @JvmStatic
    fun formatToFullDateFromUsShortDate(usShortDate: String): String? {
        var formattedString = usShortDate
        val dateFormat = usShortDateFormatter

        try {
            val date = dateFormat.parse(Strings.nullToEmpty(usShortDate))
            formattedString = localeIndependentFormatter.format(date)
        } catch (e: ParseException) {
            LOGGER.error(e.message, e)
        }

        return formattedString
    }

    /**
     * @param usShortDate a date representation in the form of "yyyy-MM-dd", this param must not be null
     *
     * @return date representation as Long or null in case of error
     */
    @JvmStatic
    fun parseUsShortDateIntoLocalTime(usShortDate: String): Long? {
        var date: Date? = null
        val dateFormat = usToLocalDateFormat
        try {
            date = dateFormat.parse(Strings.nullToEmpty(usShortDate))
        } catch (e: ParseException) {
            LOGGER.error(e.message, e)
        }

        return if (date != null) date.time else null
    }

    /**
     * @param usShortDate a date representation in the form of "yyyy-MM-dd", this param must not be null
     *
     * @return the date formatted to "MM/dd/yyyy" or null in case of error
     */
    @JvmStatic
    fun formatToUSDateWithSlashesFromUsShortDate(usShortDate: String): String? {
        var formattedString = usShortDate
        val dateFormat = usShortDateFormatter

        try {
            val date = dateFormat.parse(Strings.nullToEmpty(usShortDate))
            formattedString = LOCALE_INDEPENDENT_DATE_WITH_SLASHES.get().format(date)
        } catch (e: ParseException) {
            LOGGER.error(e.message, e)
        }

        return formattedString
    }

    /**
     * @param date date object, this param must not be null
     *
     * @return the date formatted to "MMMM dd, yyyy" in the EST5EDT timezone
     */
    @JvmStatic
    fun formatLocalFullDate(date: Date): String {
        return localFullDateFormatter.format(date)
    }

    /**
     * @param date the Long representation of the date, this param must not be null
     *
     * @return the date formatted to "MM/dd/yyyy" in the EST5EDT timezone
     */
    @JvmStatic
    fun formatToUsDateWithSlashes(date: Long): String {
        return LOCALE_INDEPENDENT_DATE_WITH_SLASHES.get().format(date)
    }

    /**
     * @param date the Long representation of the date, this param must not be null
     *
     * @return the date formatted to "MM/dd/yyyy" in the timezone of the device
     */
    @JvmStatic
    fun formatToUsDateWithSlashesWithDeviceTimeZone(date: Long): String {
        return DEVICE_TIMEZONE_DATE_WITH_SLASHES.get().format(date)
    }

    /**
     * @param date date object, this param must not be null
     *
     * @return the date formatted to "MMM dd, yyyy" in the timezone of the device
     */
    @JvmStatic
    fun formatDateWithShortMonthNameWithDeviceTimeZone(date: Long): String {
        return DEVICE_TIMEZONE_FORMAT_WITH_SHORT_MONTH_NAME.get().format(date)
    }

    /**
     * @param date the Long representation of the date, this param must not be null
     *
     * @return the date formatted to "MMM dd, yyyy" in the EST5EDT timezone
     */
    @JvmStatic
    fun formatLocaleIndependentDateWithShortMonthName(date: Long): String {
        return LOCALE_INDEPENDENT_FORMAT_WITH_SHORT_MONTH_NAME.get().format(date)
    }

    /**
     * @param date the Long representation of the date, this param must not be null
     *
     * @return the date formatted to "MM-dd-yyyy" in the EST5EDT timezone
     */
    @JvmStatic
    fun formatLocaleIndependentDateWithDashes(date: Long): String {
        return LOCALE_INDEPENDENT_DATE_WITH_DASHES.get().format(date)
    }

    /**
     * @param date the Long representation of the date, this param must not be null
     *
     * @return the date formatted to "MM/dd/yy" in the EST5EDT timezone
     */
    @JvmStatic
    fun formatLocaleIndependentShortDateWithSlashes(date: Long): String {
        return LOCALE_INDEPENDENT_SHORT_DATE_WITH_SLASHES.get().format(date)
    }

    /**
     * @param beforeOrAfter the string representation of the MarketEventTime or null
     *
     * @return the string representation of the MarketEventTime or "N/A"
     */
    @JvmStatic
    fun formatMarketEventTime(beforeOrAfter: String?): String {
        return MarketEventTime.safeValueOf(beforeOrAfter).formatted
    }

    /**
     * @param date date object, this param must not be null
     *
     * @return the date formatted to "MM/dd/yyyy" in the EST5EDT timezone
     */
    @JvmStatic
    fun formatToShortDate(date: Date): String {
        return DISPLAYED_DATE_FORMAT.get().format(date)
    }

    /**
     * @param date the Long representation of the date, this param must not be null
     *
     * @return the date formatted to "MM/dd/yyyy" in the EST5EDT timezone
     */
    @JvmStatic
    fun formatToShortDate(date: Long): String {
        return DISPLAYED_DATE_FORMAT.get().format(date)
    }

    /**
     * @param date the Long representation of the date, this param must not be null
     *
     * @return the date formatted to "MM/dd/yyyy" in the timezone of the device
     */
    @JvmStatic
    fun formatToShortDateWithDeviceTimeZone(date: Long): String {
        return DEVICE_TIMEZONE_DATE_FORMAT.get().format(date)
    }

    /**
     * @param date the String representation of the date in the format of "yyyy-MM-dd" or null
     *
     * @return the date formatted to "MM/dd/yyyy" in the EST5EDT timezone or null in case of error
     */
    @JvmStatic
    fun formatUsDateToDisplayedDate(date: String?): String? {
        var result: String? = null
        if (date != null) {
            try {
                result = formatToShortDate(usShortDateFormatter.parse(date))
            } catch (e: ParseException) {
                LOGGER.error(e.message, e)
            }

        }
        return result
    }

    /**
     * @param time the Long representation of the date, this param must not be null
     *
     * @return the date formatted to "MMMM dd, yyyy hh:mm a z" in the EST5EDT timezone
     */
    @JvmStatic
    fun formatFullDateTime(time: Long): String {
        return fullDateTimeFormatter.format(Date(time))
    }

    /**
     * @param date the Long representation of the date, this param must not be null
     *
     * @return the date formatted to "MMMM yyyy" in the EST5EDT timezone
     */
    @JvmStatic
    fun formatLongMonthYear(date: Long): String {
        return LONG_MONTH_YEAR_FORMAT.get().format(date)
    }

    /**
     * @param date the Long representation of the date, this param must not be null
     *
     * @return the date formatted to "MMM-dd-yy" in the EST5EDT timezone
     */
    @JvmStatic
    fun formatExpirationDate(date: Long): String {
        return LOCALE_INDEPENDENT_FORMAT_EXPIRATION.get().format(date)
    }

    /**
     * @param date the Long representation of the date, this param must not be null
     *
     * @return the date formatted to "MMM-dd-yy" in the UTC timezone
     */
    @JvmStatic
    fun formatExpirationDateUtc(date: Long): String {
        return LOCALE_INDEPENDENT_FORMAT_EXPIRATION_UTC.get().format(date)
    }

    /**
     * @param time the Long representation of the date and time, this param must not be null
     *
     * @return the time formatted to "kk:mm:ss" in the EST5EDT timezone
     */
    @JvmStatic
    fun getFormattedTime(time: Long): String {
        return localeIndependentTimeFormatter.format(time)
    }

    /**
     * Enum for representing the market status
     */
    enum class MarketEventTime constructor(val formatted: String) {

        AFTERMARKET("After Market"), BEFOREMARKET("Before Market"), UNKNOWN("N/A");

        companion object {

            @JvmStatic
            fun safeValueOf(value: String?): MarketEventTime {
                return try {
                    MarketEventTime.valueOf(Strings.nullToEmpty(value).toUpperCase(Locale.US))
                } catch (ex: IllegalArgumentException) {
                    LOGGER.error(ex.message, ex)
                    UNKNOWN
                }
            }
        }
    }
}
