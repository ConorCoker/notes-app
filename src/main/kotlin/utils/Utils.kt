package utils


object Utils {

    @JvmStatic
    fun isValidIndex(index: Int, list: List<Any>): Boolean {
        return index >= 0 && index < list.size
    }

    fun isValidStatus(status: String) =
        status.lowercase() == "todo" || status.lowercase() == "done" || status.lowercase() == "doing"


}