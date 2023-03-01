import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistance.XMLSerializer
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File


private val logger = KotlinLogging.logger { }
private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
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
            20 -> save()
            21 -> load()
            0 -> exitApp()
            else -> println("Invalid option entered")
        }
    } while (true)
}

private fun load() {
    try {
        noteAPI.load()
    }catch (e:Exception){
        System.err.println("Error reading from file: $e")
    }
}

private fun save() {
    try {
        noteAPI.store()
    }catch (e:Exception){
        System.err.println("Error writing to file: $e")
    }
}

private fun exitApp() {
    logger.info { "exitApp() function has been called" }
}

private fun deleteNote() {

    if (noteAPI.numberOfNotes() > 0) {
        listNotes()
        val deletedNote =
            noteAPI.deleteNote(readNextInt("Please enter index number of note you wish to delete: "))
        if (deletedNote != null) {
            println("Delete Successful! Deleted note : ${deletedNote.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }

    } else {
        println("There is no notes in system to delete!")
    }

}

fun updateNote() {

    if (noteAPI.numberOfNotes() > 0) {
        listNotes()
        if (noteAPI.updateNote(readNextInt("Please enter index of Note you wish to update: "), createNote())) {
            println("Update Successful")
        } else println("Update Failed")

    } else println("Note notes to delete!")
}


private fun listNotes() {

    println(noteAPI.listAllNotes())

}

private fun addNote() {

    if (noteAPI.add(createNote())
    ) {
        println("Added Successfully")
    } else println("Add failed")
}

fun createNote(): Note {
    return Note(
        readNextLine("Please enter note title: "),
        readNextInt("Please enter note priority (1-5): "),
        readNextLine("Please enter note category: "),
        false
    )
}


private fun displayMenuAndReturnInput(): Int {
    return readNextInt(
        """
         > ----------------------------------
         > |        NOTE KEEPER APP         |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a note                |
         > |   2) List all notes            |
         > |   3) Update a note             |
         > |   4) Delete a note             |
         > |  20) Save notes                |
         > |  21) Load notes                |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">")
    )
}