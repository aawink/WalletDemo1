package com.etrade.eo.core.util

import com.etrade.eo.core.util.DateFormattingUtils.formatLocaleIndependentDateWithShortMonthName
import com.etrade.eo.core.util.DateFormattingUtils.formatToFullDateFromUsShortDate
import com.etrade.eo.core.util.DateFormattingUtils.formatToShortDate
import com.etrade.eo.core.util.DateFormattingUtils.formatToUSDateWithSlashesFromUsShortDate
import com.etrade.eo.core.util.DateFormattingUtils.formatUsDateToDisplayedDate
import com.etrade.eo.core.util.DateFormattingUtils.getFormattedTime
import com.etrade.eo.core.util.DateFormattingUtils.parseDateFromString
import com.etrade.eo.core.util.DateFormattingUtils.parseUsShortDateIntoLocalTime
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import java.util.*

private const val INVALID_DATE = "qwerty"

class DateFormattingUtilsTest {

    @Test
    @Throws(Exception::class)
    fun testParseDateFromStringPm() {
        val dateString = "08/23/2015 2:34:23 pm"
        val timeInMillisResult = DateFormattingUtils.parseDateFromString(dateString)
        val expectedMillis = calculateMillis(2015, 8, 23, 14, 34, 23)
        assertEquals(expectedMillis, timeInMillisResult!!.toLong())

        assertNull(null, parseDateFromString(INVALID_DATE))

    }

    @Test
    @Throws(Exception::class)
    fun testParseDateFromStringAm() {
        val dateString = "08/23/2015 2:34:23 am"
        val timeInMillisResult = DateFormattingUtils.parseDateFromString(dateString)
        val expectedMillis = calculateMillis(2015, 8, 23, 2, 34, 23)
        assertEquals(expectedMillis, timeInMillisResult!!.toLong())

        assertNull(null, parseDateFromString(INVALID_DATE))

    }

    @Test
    @Throws(Exception::class)
    fun testParseDateFromStringAmWithBunchOfSpaces() {
        val dateString = "08/23/2015  2:34:23 am     "
        val timeInMillisResult = DateFormattingUtils.parseDateFromString(dateString)
        val expectedMillis = calculateMillis(2015, 8, 23, 2, 34, 23)
        assertEquals(expectedMillis, timeInMillisResult!!.toLong())

        assertNull(null, parseDateFromString(INVALID_DATE))

    }

    @Test
    @Throws(Exception::class)
    fun testParseDateFromStringUpperCaseAMWithSpaces() {
        val dateString = "08/23/2015  2:34:23 AM     "
        val timeInMillisResult = DateFormattingUtils.parseDateFromString(dateString)
        val expectedMillis = calculateMillis(2015, 8, 23, 2, 34, 23)
        assertEquals(expectedMillis, timeInMillisResult!!.toLong())

        assertNull(null, parseDateFromString(INVALID_DATE))
    }

    @Test
    @Throws(Exception::class)
    fun testParseDateFromStringUpperCasePMWithSpaces() {
        val dateString = "08/23/2015  2:34:23 PM     "
        val timeInMillisResult = DateFormattingUtils.parseDateFromString(dateString)
        val expectedMillis = calculateMillis(2015, 8, 23, 14, 34, 23)
        assertEquals(expectedMillis, timeInMillisResult!!.toLong())

        assertNull(null, parseDateFromString(INVALID_DATE))
    }

    @Test
    @Throws(Exception::class)
    fun testParseDateFromString24Hour() {
        val dateString = "08/23/2015 14:34:23"

        val timeInMillisResult = DateFormattingUtils.parseDateFromString(dateString)
        val expectedMillis = calculateMillis(2015, 8, 23, 14, 34, 23)
        assertEquals(expectedMillis, timeInMillisResult!!.toLong())
    }

    private fun calculateMillis(year: Int, month: Int, day: Int, hour: Int, minute: Int, sec: Int, millisecond: Int = 0): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, day, hour, minute, sec)
        calendar.set(Calendar.MILLISECOND, millisecond)
        calendar.timeZone = TimeZone.getTimeZone("EST5EDT")
        return calendar.timeInMillis
    }

    @Test
    @Throws(Exception::class)
    fun testFormatToFullDateFromUsShortDate() {

        val dateString = "2017-05-10"
        val expectedDate = "May 10, 2017"
        Assert.assertThat<String>(formatToFullDateFromUsShortDate(dateString), equalTo(expectedDate))

        Assert.assertThat<String>(formatToFullDateFromUsShortDate(INVALID_DATE), equalTo(INVALID_DATE))

    }

    @Test
    fun testParseUsShortDateIntoLocalTime() {
        val dateString = "2017-08-23"
        val timeInMillisResult = DateFormattingUtils.parseUsShortDateIntoLocalTime(dateString)
        val expectedMillis = calculateMillis(2017, 8, 23, 13, 34, 1)

        val dateExpected = Calendar.getInstance()
        dateExpected.timeInMillis = expectedMillis
        val dateActual = Calendar.getInstance()
        dateActual.timeInMillis = timeInMillisResult!!

        assertEquals(dateExpected.get(Calendar.DAY_OF_MONTH).toLong(), dateActual.get(Calendar.DAY_OF_MONTH).toLong())
        assertEquals(dateExpected.get(Calendar.MONTH).toLong(), dateActual.get(Calendar.MONTH).toLong())
        assertEquals(dateExpected.get(Calendar.YEAR).toLong(), dateActual.get(Calendar.YEAR).toLong())

        assertNull(parseUsShortDateIntoLocalTime(INVALID_DATE))
    }

    @Test
    fun testFormatToUSDateWithSlashesFromUsShortDate() {

        val dateString = "2017-05-26"

        val actualDateString = formatToUSDateWithSlashesFromUsShortDate(dateString)

        assertThat<String>(actualDateString, equalTo("05/26/2017"))

        assertThat<String>(formatToUSDateWithSlashesFromUsShortDate(INVALID_DATE), equalTo(INVALID_DATE))

    }

    @Test
    @Throws(Exception::class)
    fun testFormatLocaleIndependentDateWithShortMonthName() {
        val date = calculateMillis(2017, 1, 26, 14, 17, 53)
        val expectedDate = "Jan 26, 2017"
        Assert.assertThat(formatLocaleIndependentDateWithShortMonthName(date), equalTo(expectedDate))
    }

    @Test
    @Throws(Exception::class)
    fun testFormatMarketEventTime() {
        Assert.assertThat(DateFormattingUtils.formatMarketEventTime(
                DateFormattingUtils.MarketEventTime.AFTERMARKET.toString()),
                equalTo("After Market"))
        Assert.assertThat(DateFormattingUtils.formatMarketEventTime(
                DateFormattingUtils.MarketEventTime.BEFOREMARKET.toString()),
                equalTo("Before Market"))
        Assert.assertThat(DateFormattingUtils.formatMarketEventTime(null),
                equalTo("N/A"))
    }

    @Test
    @Throws(Exception::class)
    fun testFormatToShortDate() {
        val date = Date(calculateMillis(2017, 1, 26, 14, 17, 53))
        val expectedDate = "01/26/2017"
        Assert.assertThat(formatToShortDate(date), equalTo(expectedDate))
    }

    @Test
    fun testFormatToShortDateWithLong() {
        val expectedDate = "01/26/2017"
        Assert.assertThat(formatToShortDate(calculateMillis(2017, 1, 26, 14, 17, 53)), equalTo(expectedDate))
    }

    @Test
    @Throws(Exception::class)
    fun testFormatUsDateToDisplayedDate() {
        val dateStr = "2017-01-26"
        val expectedDate = "01/26/2017"
        Assert.assertThat<String>(formatUsDateToDisplayedDate(dateStr), equalTo(expectedDate))

        assertNull(formatUsDateToDisplayedDate(INVALID_DATE))
    }

    @Test
    fun testGetFormattedTime() {
        assertThat(getFormattedTime(1495832245631L), equalTo("16:57:25"))
    }

    @Test
    fun testFormatLongMonthYear() {
        assertThat(DateFormattingUtils.formatLongMonthYear(1506515062000L), `is`(equalTo("September 2017")))
    }

}
