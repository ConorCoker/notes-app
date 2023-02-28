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

    fun deleteNote(index: Int): Note? {

        return if (Utils.isValidIndex(index, notes)) {
            notes.removeAt(index)
        } else null
    }

    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {

        return if (Utils.isValidIndex(indexToUpdate, notes) && note != null) {
            findNote(indexToUpdate)!!.noteTitle = note.noteTitle
            findNote(indexToUpdate)!!.notePriority = note.notePriority
            findNote(indexToUpdate)!!.noteCategory = note.noteCategory
            findNote(indexToUpdate)!!.isNoteArchived = note.isNoteArchived
            true
        } else false
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

    fun listNotesBySelectedPriority(priority: Int) =

        if (notes.isNotEmpty()) {

            if (numberOfNotesByPriority()[priority]!! > 0) {
                var str = ""
                for (note in notes) {
                    if (note.notePriority == priority) {
                        str += "${notes.indexOf(note)}: $note\n"
                    }
                }
                str
            } else {
                "No notes for that priority stored"
            }
        } else "No notes stored"


    private fun numberOfNotesByPriority(): HashMap<Int, Int> {

        val mapToReturn = HashMap<Int, Int>()
        mapToReturn[1] = 0
        mapToReturn[2] = 0
        mapToReturn[3] = 0
        mapToReturn[4] = 0
        mapToReturn[5] = 0
        for (note in notes) {
            when (note.notePriority) {
                1 -> mapToReturn[1] = mapToReturn[1]!! + 1
                2 -> mapToReturn[2] = mapToReturn[2]!! + 1
                3 -> mapToReturn[3] = mapToReturn[3]!! + 1
                4 -> mapToReturn[4] = mapToReturn[4]!! + 1
                5 -> mapToReturn[5] = mapToReturn[5]!! + 1
            }
        }
        return mapToReturn
    }


}



