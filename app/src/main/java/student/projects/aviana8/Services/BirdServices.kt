package student.projects.aviana8.Services

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query
import student.projects.aviana8.Data.HotspotEntity
import student.projects.aviana8.Data.SightingEntity

// 1. API Response Models
data class BirdHotspot(
    @SerializedName("locId") val locId: String,
    @SerializedName("locName") val locName: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("numSpeciesAllTime") val numSpeciesAllTime: Int,
    @SerializedName("latestObsDt") val latestObsDt: String
)

data class NotableBirdSightings(
    @SerializedName("comName") val commonName: String,
    @SerializedName("locName") val locationName: String,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lng") val longitude: Double,
    @SerializedName("obsDt") val observationDate: String
)

// 2. API Interface
interface IBirdHotspot {
    @GET("/v2/ref/hotspot/geo?&fmt=json")
    suspend fun getHotspotLocations(
        @HeaderMap ebirdKey: Map<String, String>,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
    ): Response<List<BirdHotspot>>

    @GET("v2/data/obs/geo/recent/notable")
    suspend fun getNotableBirdSightings(
        @HeaderMap ebirdKey: Map<String, String>,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("maxResults") maxResults: Int,
    ): Response<List<NotableBirdSightings>>
}

// 3. Retrofit Client
object HotspotAPIClient {
    val HotspotService: IBirdHotspot = Retrofit.Builder()
        .baseUrl("https://api.ebird.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(IBirdHotspot::class.java)
}

// Extension functions for conversion
fun BirdHotspot.toEntity(): HotspotEntity {
    return HotspotEntity(
        locId = this.locId,
        locName = this.locName,

        lat = this.lat,
        lng = this.lng,
        latestObsDt = this.latestObsDt,
        numSpeciesAllTime = this.numSpeciesAllTime,

    )
}

fun HotspotEntity.toBirdHotspot(): BirdHotspot {
    return BirdHotspot(
        locId = this.locId,
        locName = this.locName,

        lat = this.lat,
        lng = this.lng,
        latestObsDt = this.latestObsDt,
        numSpeciesAllTime = this.numSpeciesAllTime,

    )
}

/*fun NotableBirdSightings.toEntity(): SightingEntity {
    return SightingEntity(
        commonName = this.commonName,


        latitude = this.latitude,
        longitude = this.longitude
        //observationDate = this.observationDate

    )
}*/

fun SightingEntity.toNotableSighting(): NotableBirdSightings {
    return NotableBirdSightings(
        commonName = this.commonName,

        locationName = this.locationName,
        latitude = this.latitude,
        longitude = this.longitude,
        observationDate = this.observationDate,

    )
}