package edu.rosehulman.todoheap.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class FreeEvent(var name: String = "",
                    var isOneSitting: Boolean = false,
                    var priority: Int = 0,
                    var procrastination: Int = 0,
                    var enjoyability: Int = 0,
                     var workload: Double = 0.0,
                    var location: String? = null,
                    var deadline: Date? = null) : Parcelable
{
    @get: Exclude
    var id: String? = null

    companion object {
        @JvmStatic
        fun fromSnapshot(snapshot: QueryDocumentSnapshot): FreeEvent {
            return snapshot.toObject(FreeEvent::class.java).also { it.id = snapshot.id }
        }
    }
}