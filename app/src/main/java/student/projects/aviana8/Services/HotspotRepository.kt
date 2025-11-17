package student.projects.aviana8.Services

import android.content.Context
import student.projects.aviana8.Data.BirdDatabase

class HotspotRepository(
    private val hotspotService: IBirdHotspot,
    private val database: BirdDatabase,
    private val networkMonitor: NetworkMonitor,
    private val context: Context
) {
    private val hotspotDao = database.hotspotDao()
  //  private val sightingDao = database.sightingDao()

    suspend fun getNearbyHotspots(lat: Double, lng: Double, apiKey: String): Result<List<BirdHotspot>> {
        return try {
            if (networkMonitor.isConnected()) {
                // ONLINE: Fetch from API and cache
                val headers = mapOf("X-eBirdApiToken" to "rhj2pqdjsgpu")
                val response = hotspotService.getHotspotLocations(headers, lat, lng)

                if (response.isSuccessful) {
                    val hotspots = response.body() ?: emptyList()

                    // Cache to database (replace old data)
                    val entities = hotspots.map { it.toEntity() }
                    hotspotDao.deleteAllHotspots() // Clear old cache
                    hotspotDao.insertHotspots(entities) // Save new data

                    Result.success(hotspots)
                } else {
                    // API failed, try cached data
                    getCachedHotspots()
                }
            } else {
                // OFFLINE: Return cached data
                getCachedHotspots()
            }
        } catch (e: Exception) {
            // On any error, try cached data
            getCachedHotspots()
        }
    }

    private suspend fun getCachedHotspots(): Result<List<BirdHotspot>> {
        return try {
            val cached = hotspotDao.getAllHotspots()
            if (cached.isNotEmpty()) {
                Result.success(cached.map { it.toBirdHotspot() })
            } else {
                Result.failure(Exception("No cached data available"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}