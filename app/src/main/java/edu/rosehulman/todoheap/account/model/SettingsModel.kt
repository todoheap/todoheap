package edu.rosehulman.todoheap.account.model

import android.util.Log
import android.widget.SeekBar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ListenerRegistration
import edu.rosehulman.todoheap.Constants
import edu.rosehulman.todoheap.data.Database
import edu.rosehulman.todoheap.databinding.FragmentAccountBinding

class SettingsModel {
    var showNotifications = false
        set(value) {
            if(field!=value){
                field = value
                updateNotificationSetting()
            }
        }

    var freeNotificationTime: Int = 0
        set(value) {
            if(field!=value){
                field = value
                updateNotificationSetting()
            }
        }
    var scheduledNotificationTime: Int = 0
        set(value) {
            if(field!=value){
                field = value
                updateNotificationSetting()
            }
        }

    var dueTimeWeight = 0

    var priorityWeight = 0

    var procrastinationWeight = 0

    var enjoyabilityWeight = 0

    var workloadWeight = 0
//        set(value) {
//            if(field!=value){
//                field = value
//                updateWeightSettings()
//            }
//        }

    var isSeekBarMoving = false

    var seekBarChangeListener = object: SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            updateWeightSettings()
            Log.d(Constants.TAG, "weights changed")
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            //isSeekBarMoving = true
            Log.d(Constants.TAG,"Starts moving")
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            //isSeekBarMoving = false
            
            Log.d(Constants.TAG,"Stops moving")
        }

    }

    private fun updateNotificationSetting(){
        Database.notificationSettingsDocument?.set(NotificationSettingModel(showNotifications, freeNotificationTime,scheduledNotificationTime))
    }

    private fun updateWeightSettings() {
        if(isSeekBarMoving)return
        Database.weightSettingsDocument?.set(WeightSettingModel(
                dueTimeWeight, priorityWeight,procrastinationWeight,enjoyabilityWeight,workloadWeight
        ))
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

    private fun initNotifications(model: NotificationSettingModel){
        showNotifications = model.enable
        freeNotificationTime = model.freeNotificationTime
        scheduledNotificationTime = model.scheduledNotificationTime
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
            val model = snapshot.toObject(NotificationSettingModel::class.java)?:NotificationSettingModel()
            initNotifications(model)
        }
        weightListenerRegistration = Database.weightSettingsDocument?.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e(Constants.TAG, "Error in SettingsModel weight listener: $error")
                return@addSnapshotListener
            }
            val snapshot = value ?: return@addSnapshotListener
            val weightObject = snapshot.toObject(WeightSettingModel::class.java) ?: WeightSettingModel()
            initSliderValues(weightObject)

        }
    }

}