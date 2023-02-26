package controllers

import models.Note

class NoteAPI {

    private var notes = ArrayList<Note>()

    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun listAllNotes(): String {

        return if (notes.isNotEmpty()) {
            var str = ""
            for (note in notes) {
                str += "${notes.indexOf(note)}: $note\n"
            }
            str
        } else "No notes stored"
    }

}