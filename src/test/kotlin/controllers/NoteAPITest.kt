package controllers

import persistance.YamlSerializer
import models.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistance.JSONSerializer
import persistance.XMLSerializer
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class NoteAPITest {

    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))
    private var emptyNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))

    @BeforeEach
    fun setup() {
        learnKotlin = Note("Learning Kotlin", 5, "College", false)
        summerHoliday = Note("Summer Holiday to France", 1, "Holiday", false)
        codeApp = Note("Code App", 4, "Work", false)
        testApp = Note("Test App", 4, "Work", false)
        swim = Note("Swim - Pool", 3, "Hobby", false)

        //adding 5 models.models.models.Note to the notes api
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

    @Nested
    inner class UpdatingNotes {

        @Test
        fun `updating a note that does not exist returns false`() {
            assertFalse(emptyNotes!!.updateNote(0, Note("Updated models.models.models.Note", 1, "work", false)))
            assertFalse(populatedNotes!!.updateNote(6, Note("Updated models.models.models.Note", 1, "work", false)))
            assertFalse(populatedNotes!!.updateNote(-1, Note("Updated models.models.models.Note", 1, "work", false)))
        }

        @Test
        fun `updating a note that exists updates that note and returns true`() {

            assertEquals(swim, populatedNotes!!.findNote(4))
            assertEquals("Swim - Pool", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(3, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("Hobby", populatedNotes!!.findNote(4)!!.noteCategory)
            assertTrue(!populatedNotes!!.findNote(4)!!.isNoteArchived)


            assertTrue(populatedNotes!!.updateNote(4, Note("Updating models.models.models.Note", 2, "College", true)))
            assertEquals("Updating models.models.models.Note", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(2, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("College", populatedNotes!!.findNote(4)!!.noteCategory)
            assertTrue(populatedNotes!!.findNote(4)!!.isNoteArchived)

        }

    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.store()

            //Loading the empty notes.xml file into a new object
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 notes to the notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.xml into a different collection
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))

            //The above assertEquals only work because models.models.models.Note is a data class so its equals() method does not care about
            //reference in memory only comparing fields

        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.store()

            //Loading the empty notes.json file into a new object
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 notes to the notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.json into a different collection
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }

        @Test
        fun `saving and loading an empty collection in YAML does not crash app and loads back empty list`() {
            val emptyNoteAPI = NoteAPI(YamlSerializer(File("notes.yaml")))
            emptyNoteAPI.store()
            val loadedEmptyNoteAPI = NoteAPI(YamlSerializer(File("notes.yaml")))
            assertEquals(0, loadedEmptyNoteAPI.numberOfNotes())
            assertEquals(emptyNoteAPI.numberOfNotes(), loadedEmptyNoteAPI.numberOfNotes())
        }

        @Test
        fun `saving then loading notes in YAML does not lose data`() {
            //new note api and adding 3 notes to it
            val noteAPIToSave = NoteAPI(YamlSerializer(File("notes.yaml")))
            noteAPIToSave.add(learnKotlin!!)
            noteAPIToSave.add(summerHoliday!!)
            noteAPIToSave.add(codeApp!!)
            //saving this noteAPI with the 3 notes inside
            noteAPIToSave.store()
            //creating new instance of note api
            val noteAPIToLoad = NoteAPI(YamlSerializer(File("notes.yaml")))
            //loading the old save from original instance to this noteAPI
            noteAPIToLoad.load()
            //checking are all notes present and equal
            assertEquals(noteAPIToSave.numberOfNotes(), noteAPIToLoad.numberOfNotes())
            assertEquals(noteAPIToSave.findNote(0), noteAPIToLoad.findNote(0))
            assertEquals(noteAPIToSave.findNote(1), noteAPIToSave.findNote(1))
            assertEquals(noteAPIToSave.findNote(2), noteAPIToLoad.findNote(2))
        }

    }

    @Nested
    inner class ArchivingNotes {

        @Test
        fun `trying to archive a note when theres no notes in system returns -999`() {
            assertEquals(-999, emptyNotes!!.archiveNote(0))
        }

        @Test
        fun `trying to archive a note in a populated list but with a invalid index returns -999`() {
            assertEquals(-999, populatedNotes!!.archiveNote(5))
        }

        @Test
        fun `archiving an active note archives that note and archiving a note that is archived returns -1`() {
            assertFalse(populatedNotes!!.findNote(0)!!.isNoteArchived)
            populatedNotes!!.archiveNote(0)
            assertTrue(populatedNotes!!.findNote(0)!!.isNoteArchived)
            assertEquals(-1, populatedNotes!!.archiveNote(0))
        }
    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfNotesCalculatedCorrectly() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(0, emptyNotes!!.numberOfNotes())
        }

        @Test
        fun numberOfArchivedNotesCalculatedCorrectly() {
            //currently all notes are active
            assertEquals(0, populatedNotes!!.numberOfArchivedNotes())
            //making this note archived
            learnKotlin!!.isNoteArchived = true
            //checking do we now have 1 archived note
            assertEquals(1, populatedNotes!!.numberOfArchivedNotes())
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
        }

        @Test
        fun numberOfActiveNotesCalculatedCorrectly() {
            assertEquals(5, populatedNotes!!.numberOfActiveNotes())
            learnKotlin!!.isNoteArchived = true
            assertEquals(4, populatedNotes!!.numberOfActiveNotes())
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
        }

        @Test
        fun numberOfNotesByPriorityCalculatedCorrectly() {
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority()[1])
            assertEquals(0, populatedNotes!!.numberOfNotesByPriority()[2])
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority()[3])
            assertEquals(2, populatedNotes!!.numberOfNotesByPriority()[4])
            assertEquals(1, populatedNotes!!.numberOfNotesByPriority()[5])

            assertEquals(0, emptyNotes!!.numberOfNotesByPriority()[1])


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
        @Test
        fun `listActiveNotes returns no active notes stored when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfActiveNotes())
            assertTrue(
                emptyNotes!!.listActiveNotes().lowercase().contains("no active notes")
            )
        }

        @Test
        fun `listActiveNotes returns active notes when ArrayList has active notes stored`() {
            //currently all notes are active so we expect 5 active notes
            assertEquals(5, populatedNotes!!.numberOfActiveNotes())
            //setting two apps to be archived
            codeApp!!.isNoteArchived = true
            swim!!.isNoteArchived = true
            assertEquals(3,populatedNotes!!.numberOfActiveNotes())
            //calling listActiveNotes and expecting only 3 notes to be listed
            val activeNotesString = populatedNotes!!.listActiveNotes().lowercase()
            assertTrue(activeNotesString.contains("learning kotlin"))
            assertFalse(activeNotesString.contains("code app"))
            assertTrue(activeNotesString.contains("summer holiday"))
            assertTrue(activeNotesString.contains("test app"))
            assertFalse(activeNotesString.contains("swim"))
        }

        @Test
        fun `listArchivedNotes returns no archived notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfArchivedNotes())
            assertTrue(
                emptyNotes!!.listArchivedNotes().lowercase().contains("no archived notes")
            )
        }

        @Test
        fun `listArchivedNotes returns archived notes when ArrayList has archived notes stored`() {
            //setting two apps to be archived
            codeApp!!.isNoteArchived = true
            swim!!.isNoteArchived = true
            assertEquals(2, populatedNotes!!.numberOfArchivedNotes())
            val archivedNotesString = populatedNotes!!.listArchivedNotes().lowercase()
            assertFalse(archivedNotesString.contains("learning kotlin"))
            assertTrue(archivedNotesString.contains("code app"))
            assertFalse(archivedNotesString.contains("summer holiday"))
            assertFalse(archivedNotesString.contains("test app"))
            assertTrue(archivedNotesString.contains("swim"))
        }

        @Test
        fun `listNotesBySelectedPriority returns No Notes when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listNotesBySelectedPriority(1).lowercase().contains("no notes")
            )
        }

        @Test
        fun `listNotesBySelectedPriority returns no notes when no notes of that priority exist`() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val priority2String = populatedNotes!!.listNotesBySelectedPriority(2).lowercase()
            assertTrue(priority2String.contains("no notes"))
            assertTrue(priority2String.contains("2"))
        }

        @Test
        fun `listNotesBySelectedPriority returns all notes that match that priority when notes of that priority exist`() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val priority1String = populatedNotes!!.listNotesBySelectedPriority(1).lowercase()
            assertTrue(priority1String.contains("summer holiday"))
            assertFalse(priority1String.contains("swim"))
            assertFalse(priority1String.contains("learning kotlin"))
            assertFalse(priority1String.contains("code app"))
            assertFalse(priority1String.contains("test app"))


            val priority4String = populatedNotes!!.listNotesBySelectedPriority(4).lowercase()
            assertFalse(priority4String.contains("swim"))
            assertTrue(priority4String.contains("code app"))
            assertTrue(priority4String.contains("test app"))
            assertFalse(priority4String.contains("learning kotlin"))
            assertFalse(priority4String.contains("summer holiday"))
        }
    }



}
