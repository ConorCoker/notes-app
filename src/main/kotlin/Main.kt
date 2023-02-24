import java.util.Scanner

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
    TODO("Not yet implemented")
}

private fun deleteNote() {
    TODO("Not yet implemented")
}

private fun updateNote() {
    TODO("Not yet implemented")
}

private fun listNotes() {
    TODO("Not yet implemented")
}

private fun addNote() {
    TODO("Not yet implemented")
}

private fun displayMenuAndReturnInput(): Int {

    val scanner = Scanner(System.`in`)

    println(
        """
        --------------------
        NOTE KEEPER APP
        --------------------
        NOTE MENU
            1) Add a note
            2) List all notes
            3) Update a note
            4) Delete a note
        --------------------
            0) Exit
        --------------------    
    """.trimIndent()
    )
    print("==>> ")
    return scanner.nextInt()
}