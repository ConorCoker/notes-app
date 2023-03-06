import controllers.NoteAPI
import models.Note
import mu.KotlinLogging
import persistance.YamlSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import utils.Utils
import java.io.File


private val logger = KotlinLogging.logger { }

//private val noteAPI = NoteAPI(XMLSerializer(File("notes.xml")))
//private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))
private val noteAPI = NoteAPI(YamlSerializer(File("notes.yaml")))

fun main(args: Array<String>) {

    runMenu()

}

private fun runMenu() {
    do {
        when (displayMenuAndReturnInput()) {
            1 -> addNote()
            2 -> runSubMenu()
            3 -> updateNoteStatus()
            4 -> deleteNote()
            5 -> archiveNote()
            6 -> searchForNoteByTitle()
            20 -> save()
            21 -> load()
            0 -> exitApp()
            else -> println("Invalid option entered")
        }
    } while (true)
}

private fun searchForNoteByTitle() {
    val stringToPrint =
        noteAPI.searchByTitle(readNextLine("Please enter the title of the note you wish to search for: "))
    if (stringToPrint.isEmpty()) println("No note with that title has been found!") else println(stringToPrint)
}

private fun runSubMenu() {
    do {
        when (displaySubMenuAndReturnInput().lowercaseChar()) {

            'a' -> listNotes()
            'b' -> listActiveNotes()
            'c' -> listArchivedNotes()
            'd' -> listNumberOfNotesByPriority()
            'e' -> listNotesBySelectedPriority()
            'f' -> runMenu()
            else -> println("Invalid option entered")
        }
    } while (true)
}

fun listNotesBySelectedPriority() {
    println(noteAPI.listNotesBySelectedPriority(readNextInt("Enter priority of notes you wish to display: ")))
}

private fun listNumberOfNotesByPriority() {
    println(noteAPI.numberOfNotesByPriority())
}

private fun listArchivedNotes() {
    println(noteAPI.listArchivedNotes())
}

private fun listActiveNotes() {
    println(noteAPI.listActiveNotes())
}

private fun displaySubMenuAndReturnInput(): Char {

    return ScannerInput.readNextChar(
        """
        >|-------------------------------------|
        >| a) List all notes                   |
        >| b) List active notes                |
        >| c) List archived notes              |            
        >| d) List number of notes by priority |
        >| e) List notes of a specific priority|
        >| f) Exit sub menu                    |
        >|-------------------------------------|
        >==>> """.trimMargin(">")
    )
}

private fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

private fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
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

private fun archiveNote() {
    if (noteAPI.numberOfNotes() > 0) {
        listActiveNotes()
        val index = readNextInt("Please enter index of note you wish to archive: ")
        when (noteAPI.archiveNote(index)) {
            1 -> println("Note $index has been archived!")
            -1 -> println("Note $index is already archived!")
            -999 -> println("Invalid index. Note could not be archived.")
        }
    } else {
        println("There are no notes for you to archive!")
    }

}

private fun updateNoteStatus() {

    if (noteAPI.numberOfNotes() > 0) {
        listNotes()
        when (noteAPI.updateNote(
            readNextInt("Please enter index of note you want to update the status of: "),
            readNextLine("Please enter status of note you wish to update: (todo) (doing) (done) ")
        )) {
            1 -> {
                println("Note status updated!")
            }

            -1 -> {
                println("Status not valid please try again!")
                updateNoteStatus()
            };
            0 -> {
                println("Index is not valid! Please try again")
                updateNoteStatus()
            }

            else -> {
                println("Something went wrong!")
            }
        }


    } else {
        println("There are no notes in the system for you to update!")
    }
}


private fun listNotes() = println(noteAPI.listAllNotes())
private fun addNote() {

    if (noteAPI.add(createNote())
    ) {
        println("Added Successfully")
    } else println("Add failed")
}

private fun createNote(): Note {

    val title = readNextLine("Please enter note title (e.g. go shopping): ")
    var priority = 0
    do {
        priority = readNextInt("Please enter note priority (1-5): ")
    } while (!Utils.isValidPriority(priority))
    var category = ""
    do {
        category = readNextLine("Please enter note category (work) (school) (personal) (home): ")
    } while (!Utils.isValidCategory(category))
    return Note(title, priority, category)
}

private fun displayMenuAndReturnInput(): Int {
    return readNextInt(
        """
         > --------------------------------------------
         > |             NOTE KEEPER APP              |
         > --------------------------------------------
         > | NOTE MENU                                |
         > |   1) Add a note                          |
         > |   2) List notes                          |
         > |      a) List all notes                   |
         > |      b) List active notes                |
         > |      c) List archived notes              |            
         > |      d) List number of notes by priority |
         > |      e) List notes of a specific priority|
         > |   3) Update note status                  |
         > |   4) Delete a note                       |
         > |   5) Archive a note                      |
         > |   6) Search for a note by title          |
         > |  20) Save notes                          |
         > |  21) Load notes                          |
         > --------------------------------------------
         > |   0) Exit                                |
         > --------------------------------------------
         > ==>> """.trimMargin(">")
    )
}