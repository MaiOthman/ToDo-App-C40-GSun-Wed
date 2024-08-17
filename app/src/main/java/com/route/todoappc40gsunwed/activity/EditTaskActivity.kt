package com.route.todoappc40gsunwed.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.route.todoappc40gsunwed.Constants
import com.route.todoappc40gsunwed.adapter.TaskAdapter
import com.route.todoappc40gsunwed.callbacks.OnTaskAddedListener
import com.route.todoappc40gsunwed.database.TaskDatabase
import com.route.todoappc40gsunwed.database.model.Task
import com.route.todoappc40gsunwed.databinding.ActivityEditTaskBinding
import java.util.Calendar

class EditTaskActivity:AppCompatActivity() {
    lateinit var binding: ActivityEditTaskBinding
    lateinit var intentTask: Task
    lateinit var calendar: Calendar
    var isDateSelected = false
    var isTimeSelected = false
    var onTaskUpdatedListener: OnTaskAddedListener? = null
    lateinit var datePicker: DatePickerDialog
    lateinit var timePicker: TimePickerDialog
    lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        calendar = Calendar.getInstance()
        intentTask = IntentCompat.getParcelableExtra(intent, Constants.TASK, Task::class.java)!!
        binding.titleEditText.setText(intentTask.title)
        binding.descriptionEditText.setText(intentTask.description)
        binding.selectTaskDateText.setOnClickListener {
            datePickerfun()
        }
        binding.selectTaskTimeText.setOnClickListener {
         timePickerfun()
        }
        task = intentTask

        binding.addButton.setOnClickListener{
            task.apply {
                title = binding.titleEditText.text.toString()
                description = binding.descriptionEditText.text.toString()


            }
            TaskDatabase.getInstance(this).getTaskDao().updateTask(task)
            onTaskUpdatedListener?.onTaskAdded()
            finish()
        }
    }

    private fun timePickerfun() {
        timePicker =
            TimePickerDialog(
                this,
                { view, hourOfDay, minute ->
                    isTimeSelected = true
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)

                    var hour = hourOfDay % 12
                    if (hour == 0) hour = 12
                    val AM_PM = if ((hourOfDay > 12)) "PM" else "AM"
                    binding.selectTaskTimeText.text = "${hour}:${minute} $AM_PM"
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                false
            )
        timePicker.show()
    }

    private fun datePickerfun(){
        datePicker = DatePickerDialog(this)
        datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
            isDateSelected = true
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            binding.selectTaskDateText.text = "$dayOfMonth/${month + 1}/$year"
        }
        datePicker.show()
    }
}