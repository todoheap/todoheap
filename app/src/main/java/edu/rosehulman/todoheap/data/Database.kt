package edu.rosehulman.todoheap.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object Database {
    private const val COLLECTION_USERS = "users"
    private const val COLLECTION_FREE_EVENTS = "free_events"
    private const val COLLECTION_SCHEDULED_EVENTS = "scheduled_events"

    private val userCollection = FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
    val auth = FirebaseAuth.getInstance()


    fun signOut() = auth.signOut()

    //val uid get() = auth.uid

    val freeEventsCollection get() = userCollection.document(auth.uid?:"").collection(COLLECTION_FREE_EVENTS)
    val scheduledEventsCollection get() = userCollection.document(auth.uid?:"").collection(COLLECTION_SCHEDULED_EVENTS)

}