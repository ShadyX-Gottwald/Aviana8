package student.projects.aviana8.Data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

// 1. Database Entities
@Entity(tableName = "hotspots")
data class HotspotEntity(
    @PrimaryKey val locId: String,
    val locName: String,
    val lat: Double,
    val lng: Double,
    val numSpeciesAllTime: Int,
    val latestObsDt: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "sightings")
data class SightingEntity(
    @PrimaryKey val id: String,
    val commonName: String,
    val locationName: String,
    val latitude: Double,
    val longitude: Double,
    val observationDate: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

// 2. Data Access Objects (DAOs)
@Dao
interface HotspotDao {
    @Query("SELECT * FROM hotspots")
    suspend fun getAllHotspots(): List<HotspotEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHotspots(hotspots: List<HotspotEntity>)

    @Query("DELETE FROM hotspots")
    suspend fun deleteAllHotspots()
}

// 3. Room Database
@Database(entities = [HotspotEntity::class, SightingEntity::class], version = 1)
abstract class BirdDatabase : RoomDatabase() {
    abstract fun hotspotDao(): HotspotDao
   // abstract fun sightingDao(): SightingDao

    companion object {
        fun getInstance(context: Context): BirdDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                BirdDatabase::class.java,
                "bird_database"
            ).build()
        }
    }
}