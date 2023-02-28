package controllers

import models.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NoteAPITest {

    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI()
    private var emptyNotes: NoteAPI? = NoteAPI()

    @BeforeEach
    fun setup() {
        learnKotlin = Note("Learning Kotlin", 5, "College", false)
        summerHoliday = Note("Summer Holiday to France", 1, "Holiday", false)
        codeApp = Note("Code App", 4, "Work", false)
        testApp = Note("Test App", 4, "Work", false)
        swim = Note("Swim - Pool", 3, "Hobby", false)

        //adding 5 Note to the notes api
        populatedNotes!!.add(learnKotlin!!)
        populatedNotes!!.add(summerHoliday!!)
        populatedNotes!!.add(codeApp!!)
        populatedNotes!!.add(testApp!!)
        populatedNotes!!.add(swim!!)
    }

    @AfterEach
    fun tearDown() {
        learnKotlin = null
        summerHoliday = null
        codeApp = null
        testApp = null
        swim = null
        populatedNotes = null
        emptyNotes = null
    }

    @Nested
    inner class AddNotes {
        @Test
        fun `adding a Note to a populated list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(newNote, populatedNotes!!.findNote(populatedNotes!!.numberOfNotes() - 1))
        }

        @Test
        fun `adding a Note to an empty list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false)
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.add(newNote))
            assertEquals(1, emptyNotes!!.numberOfNotes())
            assertEquals(newNote, emptyNotes!!.findNote(emptyNotes!!.numberOfNotes() - 1))
        }
    }

    @Nested
    inner class ListNotes {

        @Test
        fun `listAllNotes returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listAllNotes().lowercase().contains("no notes"))
        }

        @Test
        fun `listAllNotes returns Notes when ArrayList has notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listAllNotes().lowercase()
            assertTrue(notesString.contains("learning kotlin"))
            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("swim"))
            assertTrue(notesString.contains("summer holiday"))
        }
    }

    @Nested
    inner class ListActiveOrInactiveNotes {

        @Test
        fun `list all archived notes returns No archived notes stored message when no archived notes are stored `() {
            val archivedNotesString = populatedNotes!!.listArchivedNotes()
            assertTrue(archivedNotesString.contains("No archived notes stored"))
        }

        @Test
        fun `list all archived notes returns archived notes when archived notes are present in collection`() {
            populatedNotes!!.add(Note("Archived note", 1, "Archived", true))
            assertTrue(populatedNotes!!.listArchivedNotes().contains("Archived note"))
        }

        @Test
        fun `list all active notes returns no active notes stored when no active notes are present`() {
            assertTrue(emptyNotes!!.listActiveNotes().contains("No active notes stored"))
        }

        @Test
        fun `list all active notes returns the active notes toString when active notes are present in collection`() {
            val activeString = populatedNotes!!.listActiveNotes()
            assertTrue(activeString.lowercase().contains("learning kotlin"))
        }


    }

    @Nested
    inner class ListNotesByPriority {

        @Test
        fun `list notes by priority returns correct message when NO notes of that priority stored but notes are present in collection`() {
            assertTrue(populatedNotes!!.listNotesBySelectedPriority(2).contains("No notes for that priority stored"))
        }

        @Test
        fun `list notes by priority returns correct message when NO notes are in collection`() {
            assertTrue(emptyNotes!!.listNotesBySelectedPriority(2).contains("No notes stored"))
        }

        @Test
        fun `list notes by priority returns correct note of that priority`() {
            assertTrue(
                populatedNotes!!.listNotesBySelectedPriority(4)
                    .contains("Test App") && populatedNotes!!.listNotesBySelectedPriority(4).contains("Code App")
            )
        }

    }

    @Nested
    inner class DeleteNotes {

        @Test
        fun `deleting a note when no notes present returns null`() {
            assertEquals(null, emptyNotes!!.deleteNote(2))
        }

        @Test
        fun `deleting a note when notes are present but a invalid index is supplied returns null`() {
            assertEquals(null, populatedNotes!!.deleteNote(5))
        }

        @Test
        fun `deleting a valid note removes note from arraylist and returns the note`() {
            assertTrue(populatedNotes!!.numberOfNotes() == 5)
            val noteToRemove = populatedNotes!!.findNote(4)
            assertTrue(populatedNotes!!.deleteNote(4) == noteToRemove)
            assertTrue(populatedNotes!!.numberOfNotes() == 4)
        }
    }
}
