package controllers

import models.Note
import utils.Utils

class NoteAPI {

    private var notes = ArrayList<Note>()

    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun numberOfNotes():Int{
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (Utils.isValidIndex(index, notes)) {
            notes[index]
        } else null

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