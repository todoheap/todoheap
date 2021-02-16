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

    var freeNotificationTime: Int = 0
    var scheduledNotificationTime: Int = 0

    var dueTimeWeight = 0f

    var priorityWeight = 0f
    var procrastinationWeight = 0f
    var enjoyabilityWeight = 0f
    var workloadWeight = 0f

    fun updateWeightDB(value: Float) {
        Log.d("WeightDebug", "$dueTimeWeight, $value")
    }

    private var listenerRegistration: ListenerRegistration?=null
    private var weightListenerRegistration: ListenerRegistration?=null

    fun init(){
        initDBListeners()
    }

    private fun initSliderValues(model: WeightSettingModel) {
        dueTimeWeight = model.dueTimeWeight
        priorityWeight = model.priorityWeight
        procrastinationWeight = model.procrastinationWeight
        enjoyabilityWeight = model.enjoyabilityWeight
        workloadWeight = model.workloadWeight
    }

    fun initDBListeners(){
        listenerRegistration?.remove()
        weightListenerRegistration?.remove()
        listenerRegistration = Database.notificationSettingsDocument?.addSnapshotListener { value, error ->
            if(error!=null) {
                Log.e(Constants.TAG, "Error in SettingsModel: $error")
                return@addSnapshotListener
            }
            val snapshot = value ?: return@addSnapshotListener
            showNotifications = snapshot.toObject(NotificationSettingModel::class.java)?.enable?:false
        }
        weightListenerRegistration = Database.weightSettingsDocument?.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e(Constants.TAG, "Error in SettingsModel wieght listener: $error")
                return@addSnapshotListener
            }
            val snapshot = value ?: return@addSnapshotListener
            val weightObject = snapshot.toObject(WeightSettingModel::class.java) ?: WeightSettingModel()
            initSliderValues(weightObject)
        }
    }

}