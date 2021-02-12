package edu.rosehulman.todoheap.account.model

import android.util.Log
import com.google.firebase.firestore.ListenerRegistration
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.data.Database

class SettingsModel {
    var showNotifications = false
        set(value) {
            if(field!=value){
                field = value
                Database.notificationSettingsDocument?.set(NotificationSettingModel(value))
            }

            //test notification

        }
    private var listenerRegistration: ListenerRegistration?=null
    fun init(){
        listenerRegistration?.remove()
        initDBListeners()
    }

    fun initDBListeners(){
        listenerRegistration?.remove()
        listenerRegistration = Database.notificationSettingsDocument?.addSnapshotListener { value, error ->
            if(error!=null) {
                Log.e(Constants.TAG, "Error in SettingsModel: $error")
                return@addSnapshotListener
            }
            val snapshot = value ?: return@addSnapshotListener
            showNotifications = snapshot.toObject(NotificationSettingModel::class.java)?.enable?:false
        }
    }

}