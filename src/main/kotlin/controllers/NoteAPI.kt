package controllers

import models.Note
import persistance.Serializer
import utils.Utils

class NoteAPI(serializerType: Serializer) {

    private var notes = ArrayList<Note>()
    private var serializer = serializerType

    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun numberOfNotes() = notes.size

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

    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
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

    fun numberOfArchivedNotes() = notes.size - numberOfActiveNotes()


    fun numberOfActiveNotes(): Int {
//        var numOfActiveNotes = 0
//        for (note in notes) {
//            if (!note.isNoteArchived) {
//                numOfActiveNotes++
//            }
//        }
//        return numOfActiveNotes
        return notes.stream().filter { note: Note -> !note.isNoteArchived }.count().toInt()
    }

    fun archiveNote(index: Int) =
        if (Utils.isValidIndex(index, notes)) {
            if (!notes[index].isNoteArchived) {
                notes[index].isNoteArchived = true
                1
            } else -1
        } else -999

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


    fun numberOfNotesByPriority(): HashMap<Int, Int> {

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



