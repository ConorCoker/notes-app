package controllers

import models.Note
import utils.Utils

class NoteAPI {

    private var notes = ArrayList<Note>()

    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun numberOfNotes(): Int {
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

    fun listActiveNotes(): String {
        return if (numberOfActiveNotes() > 0) {
            var str = ""
            for (note in notes) {
                if (!note.isNoteArchived) {
                    str += "${notes.indexOf(note)}: $note\n"
                }
            }
            str

        } else "No active notes stored"
    }

    fun listArchivedNotes(): String {
        return if (numberOfArchivedNotes() > 0) {
            var str = ""
            for (note in notes) {
                if (note.isNoteArchived) {
                    str += "${notes.indexOf(note)}: $note\n"
                }
            }
            str

        } else return "No archived notes stored"
    }

    private fun numberOfArchivedNotes(): Int {
        return notes.size - numberOfActiveNotes()
    }

    private fun numberOfActiveNotes(): Int {
        var numOfActiveNotes = 0
        for (note in notes) {
            if (!note.isNoteArchived) {
                numOfActiveNotes++
            }
        }
        return numOfActiveNotes
    }




}