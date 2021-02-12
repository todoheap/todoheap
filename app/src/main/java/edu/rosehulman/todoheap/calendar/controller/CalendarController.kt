package edu.rosehulman.todoheap.calendar.controller

import edu.rosehulman.todoheap.R
import edu.rosehulman.todoheap.calendar.model.CalendarPageModel
import edu.rosehulman.todoheap.common.ControllerBase
import edu.rosehulman.todoheap.data.Database
import edu.rosehulman.todoheap.main.MainActivity

class CalendarController(
        val activity: MainActivity,
        val model: CalendarPageModel,
):ControllerBase(activity) {
    fun onPressPrevious(){
        model.previousDay()
    }
    fun onPressNext(){
        model.nextDay()
    }
    fun onPressSelectDate(){
        this.promptDate(model.selectedYear, model.selectedMonth,model.selectedDay){ year, month, dayOfMonth ->
            model.selectDay(year,month,dayOfMonth)
        }
    }
    fun deleteAt(position: Int){
        this.promptConfirm(R.string.title_dialog_confirm, R.string.confirm_delete,{
            Database.scheduledEventsCollection?.document(model[position].id?:"")?.delete()
        }){
            model.recyclerAdapter?.notifyItemChanged(position)
        }
    }


}