package utils


object Utils {

    @JvmStatic
    fun isValidIndex(index: Int, list: List<Any>): Boolean {
        return index >= 0 && index < list.size
    }

    @JvmStatic
    fun isValidStatus(status: String) =
        status.lowercase() == "todo" || status.lowercase() == "done" || status.lowercase() == "doing"

    @JvmStatic
    fun isValidCategory(category: String?) =
        category == "work" || category == "home" || category == "school" || category == "personal"

    @JvmStatic
    fun isValidPriority(priority: Int) = priority in 1..5

}