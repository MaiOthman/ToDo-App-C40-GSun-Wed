package com.route.todoappc40gsunwed.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.route.todoappc40gsunwed.R
import com.route.todoappc40gsunwed.callbacks.OnTaskAddedListener
import com.route.todoappc40gsunwed.clearTime
import com.route.todoappc40gsunwed.database.TaskDatabase
import com.route.todoappc40gsunwed.database.model.Task
import com.route.todoappc40gsunwed.databinding.FragmentAddTodoBinding
import java.util.Calendar

class AddTaskBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentAddTodoBinding
    lateinit var calendar: Calendar
    var isDateSelected = false
    var isTimeSelected = false
    var onTaskAddedListener: OnTaskAddedListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = Calendar.getInstance()
        initViews()
    }

    fun initViews() {

        binding.addButton.setOnClickListener {
            if (validateFields()) {
                calendar.clearTime()
                val task = Task(
                    null,
                    binding.titleEditText.text.toString(),
                    binding.descriptionEditText.text.toString(),
                    calendar.time,
                    binding.selectTaskTimeText.text.toString(),
                    false
                )

                TaskDatabase
                    .getInstance(requireContext())
                    .getTaskDao()
                    .insertTask(task)
                onTaskAddedListener?.onTaskAdded()
                dismiss()
            }
        }
        binding.selectTaskDateText.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext())
            datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
                isDateSelected = true
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.selectTaskDateText.text = "$dayOfMonth/${month + 1}/$year"
            }
            datePicker.show()
        }
        binding.selectTaskTimeText.setOnClickListener {
            val timePicker =
                TimePickerDialog(
                    requireContext(),
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
    }

    fun validateFields(): Boolean {
        if (binding.titleEditText.text.isEmpty() || binding.titleEditText.text.isBlank()) {
            binding.titleEditText.error = getString(R.string.required)
            return false
        } else
            binding.titleEditText.error = null
        if (binding.descriptionEditText.text.isEmpty() || binding.descriptionEditText.text.isBlank()) {
            binding.descriptionEditText.error = getString(R.string.required)
            return false
        } else
            binding.descriptionEditText.error = null
        if (!isTimeSelected) {
            Toast.makeText(
                requireContext(),
                getString(R.string.select_time_because_it_s_required),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (!isDateSelected) {
            Toast.makeText(
                requireContext(),
                getString(R.string.select_date_because_it_s_required), Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }
}
