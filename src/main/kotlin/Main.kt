import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import utils.ScannerInput

private val logger = KotlinLogging.logger { }
private val noteAPI = NoteAPI()
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

    if (noteAPI.numberOfNotes() > 0) {
        listNotes()
        val deletedNote = noteAPI.deleteNote(ScannerInput.readNextInt("Please enter index number of note you wish to delete: "))
        if (deletedNote!=null){
            println("Delete Successful! Deleted note : ${deletedNote.noteTitle}")
        }else{
            println("Delete NOT Successful")
        }

    } else {
        println("There is no notes in system to delete!")
    }

}

private fun updateNote() {
    logger.info { "updateNote() function has been called" }
}

private fun listNotes() {

    println(noteAPI.listAllNotes())

}

private fun addNote() {

    if (noteAPI.add(
            Note(
                ScannerInput.readNextLine("Please enter note title: "),
                ScannerInput.readNextInt("Please enter note priority (1-5): "),
                ScannerInput.readNextLine("Please enter note category: "),
                false
            )
        )
    ) {
        println("Added Successfully")
    } else println("Add failed")
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