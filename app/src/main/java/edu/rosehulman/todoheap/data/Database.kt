package edu.rosehulman.todoheap.data

import com.google.firebase.firestore.FirebaseFirestore

object Database {
    private const val COLLECTION_EVENT = "events"
    val dbEventsCollection = FirebaseFirestore.getInstance().collection(COLLECTION_EVENT)
}