package student.projects.aviana8.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class TaxonomicBird(
    @SerializedName("bandingCodes")
    val bandingCodes: List<Any> = listOf(),
    @SerializedName("category")
    val category: String = "",
    @SerializedName("comName")
    val comName: String = "",
    @SerializedName("comNameCodes")
    val comNameCodes: List<String> = listOf(),
    @SerializedName("familyCode")
    val familyCode: String = "",
    @SerializedName("familyComName")
    val familyComName: String = "",
    @SerializedName("familySciName")
    val familySciName: String = "",
    @SerializedName("order")
    val order: String = "",
    @SerializedName("sciName")
    val sciName: String = "",
    @SerializedName("sciNameCodes")
    val sciNameCodes: List<String> = listOf(),
    @SerializedName("speciesCode")
    val speciesCode: String = "",
    @SerializedName("taxonOrder")
    val taxonOrder: Double = 0.0
)

@Entity(tableName = "saved_birds")
data class SavedBird(
    @PrimaryKey
    val speciesCode: String,
    val commonName: String,
    val scientificName: String,
    val familyCommonName: String?,
    val familyScientificName: String?,
    val order: String?,
    val category: String?,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val requiredCount: Int,
    val earned: Boolean = false,
    val earnedAt: Long? = null
)
