import mu.KotlinLogging
import utils.ScannerInput

private val logger = KotlinLogging.logger {  }
fun main(args: Array<String>) {

    runMenu()

}

private fun runMenu() {
    do {
        when (displayMenuAndReturnInput()) {
            1 -> addNote()
            2 -> listNotes()
            3 -> updateNote()
            4 -> deleteNote()
            0 -> exitApp()
            else -> println("Invalid option entered")
        }
    } while (true)
}

private fun exitApp() {
    logger.info { "exitApp() function has been called" }
}

private fun deleteNote() {
    logger.info { "deleteNote() function has been called" }
}

private fun updateNote() {
    logger.info { "updateNote() function has been called" }
}

private fun listNotes() {
    logger.info { "listNotes() function has been called" }
}

private fun addNote() {
    logger.info { "addNote() function has been called" }
}

private fun displayMenuAndReturnInput(): Int {
    return ScannerInput.readNextInt(
        """
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List all notes            |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">")
    )
}