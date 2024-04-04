import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.DishType
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.persistent.Recipe
import com.fri.uniza.sk.michal.sovcik.chefsrecipies.models.transients.AppDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)


class DBTests {
    lateinit var db : AppDatabase


    private var item1 = Recipe(1,"Cokies",DishType.Dezert,30,15,4f,"Michalito","" )

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }
    private suspend fun addOneItemToDb() {
        db.recipeDao().insert(item1)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        addOneItemToDb()
        val allItems = db.recipeDao().getAllRecipies().first()
        assertEquals(allItems[0], item1)
    }

    @After
    @Throws(IOException::class)
    fun destroyDb() {
        db.close()
    }

}