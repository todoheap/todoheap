package edu.rosehulman.todoheap.common.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList
@Parcelize
data class ScheduledEvent(
    val name: String = "",
    val location: String = "",
    val startTime: Timestamp = Timestamp.now(),
    val endTime: Timestamp = Timestamp.now(),
    val repeatOn: ArrayList<Boolean> = ArrayList(),
    ) : Parcelable {
    @IgnoredOnParcel
    @get: Exclude
    var id: String? = null

    companion object {
        @JvmStatic
        fun fromSnapshot(snapshot: QueryDocumentSnapshot): ScheduledEvent {
            return snapshot.toObject(ScheduledEvent::class.java).also { it.id = snapshot.id }
        }
    }
}