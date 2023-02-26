package models

class Note(val noteTitle: String, val notePriority: Int, val noteCategory: String, val isNoteArchived: Boolean) {
    override fun toString(): String {
        return "Note(noteTitle='$noteTitle', notePriority=$notePriority, noteCategory='$noteCategory', isNoteArchived=$isNoteArchived)"
    }
}