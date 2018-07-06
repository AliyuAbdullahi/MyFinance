package latproject.com.myfinance.core.utils

/**
 * {WeekDay} is a {class} that provides an API for basic operation performed on day object
 * such as <UL><Li>Get tomorrow</Li> <Li> Get yesterday</Li></UL> etc. It provides all values as {Long}
 */
class WeekDay private constructor(day: Long) {

    private var currentDay: Long = day
    private val oneDay = 86400000L

    private val tomorrow: WeekDay = WeekDay(day + oneDay)

    private val yesterday = WeekDay(day - oneDay)

    /**
     * Returns number of days between 2 days
     * Returns 0 if it's same day
     */
    infix fun WeekDay.to(weekDay: WeekDay): Int {
        if ((Math.abs(this.currentDay - weekDay.currentDay) / 24) >= 1)
            return (Math.abs(this.currentDay - weekDay.currentDay) / 24).toInt()
        return 0
    }

    /**
     * Returns the value of one day in {Long}
     */
    fun getOneDayLongValue(): Long {
        return oneDay
    }

    /**
     * @param interval is the number of days forward
     * @param actionOn is a callback action on that date at the point of use
     */
    fun withLaterDateAt(interval: Int, actionOn: (dateLong: Long)-> Unit) {
        actionOn(currentDay + (oneDay * interval))
    }

    fun rangeTo(interval: Int): Long {
        return currentDay + (oneDay * interval)
    }

    /**
     * @param interval is the number of days back
     * @param actionOn is a callback action on that date at the point of use
     */
    fun withPreviousDateTo(interval: Int, actionOn: (dateLong: Long) -> Unit) {
        actionOn(currentDay - (oneDay * interval))
    }

    companion object {
        fun getInstance(day: Long): WeekDay {
            return WeekDay(day)
        }
    }
}