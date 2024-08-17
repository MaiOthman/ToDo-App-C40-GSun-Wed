package com.route.todoappc40gsunwed.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.WeekDayBinder
import com.route.todoappc40gsunwed.Constants
import com.route.todoappc40gsunwed.R
import com.route.todoappc40gsunwed.WeekDayViewContainer
import com.route.todoappc40gsunwed.activity.EditTaskActivity
import com.route.todoappc40gsunwed.callbacks.SetOnItemClickListner
import com.route.todoappc40gsunwed.adapter.TaskAdapter
import com.route.todoappc40gsunwed.callbacks.OnTaskAddedListener
import com.route.todoappc40gsunwed.clearTime
import com.route.todoappc40gsunwed.database.TaskDatabase
import com.route.todoappc40gsunwed.database.model.Task
import com.route.todoappc40gsunwed.databinding.FragmentTodoListBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TodoListFragment : Fragment() {
    lateinit var binding: FragmentTodoListBinding
    lateinit var adapter: TaskAdapter
    var selectedDate: LocalDate? = null
    lateinit var calendar: Calendar
    lateinit var tasks: MutableList<Task>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = Calendar.getInstance()
        initWeekCalendarView()
        getTasksFromDatabase()
    }

    override fun onStart() {
        super.onStart()
        Log.e("hallo", "This is start method")
        val tasks3 = TaskDatabase.getInstance(requireContext()).getTaskDao().getTasksByDate(calendar.time)
        adapter.updateList(tasks3)
    }


    fun getTasksFromDatabase() {
        val tasks = TaskDatabase.getInstance(requireContext())
            .getTaskDao().getAllTasks()
        adapter = TaskAdapter(tasks)
        adapter.onDoneBtnClickListner = object : SetOnItemClickListner{
            override fun onItemClick(position: Int, task: Task) {
                task.isDone = !task.isDone!!
                TaskDatabase.getInstance(requireContext()).getTaskDao().updateTask(task)
                adapter.notifyDataSetChanged()
            }
        }
        deleteTask(adapter)
        onEditListner()
        binding.tasksRecyclerView.adapter = adapter
    }

    private fun deleteTask(adapter: TaskAdapter) {
        adapter.onDeleteListner = object : SetOnItemClickListner {
            override fun onItemClick(position: Int, task: Task) {
                TaskDatabase.getInstance(requireContext()).getTaskDao().deleteTask(task)
                val tasks2 = TaskDatabase.getInstance(requireContext()).getTaskDao().getAllTasks()
                adapter.updateList(tasks2)
            }
        }
    }
    private fun onEditListner() {
        adapter.onEditListner = object: SetOnItemClickListner {
            override fun onItemClick(position: Int, task: Task) {
                var intent = Intent(requireContext(),EditTaskActivity::class.java)
                intent.putExtra(Constants.TASK, task)
                startActivity(intent)
            }
        }
    }
    fun initWeekCalendarView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bindWeekCalendarView()
            val currentDate = LocalDate.now()
            //getItemCount > 0

            val currentMonth = YearMonth.now()
            val startDate = currentMonth.minusMonths(100).atStartOfMonth() // Adjust as needed
            val endDate = currentMonth.plusMonths(100).atEndOfMonth() // Adjust as needed
            val firstDayOfWeek =
                firstDayOfWeekFromLocale(Locale.forLanguageTag("ar")) // Available from the library
            binding.weekCalendarView.setup(startDate, endDate, firstDayOfWeek)
            binding.weekCalendarView.scrollToWeek(currentDate)
        }
    }

    fun bindWeekCalendarView() {
        binding.weekCalendarView.dayBinder = object : WeekDayBinder<WeekDayViewContainer> {

            override fun bind(container: WeekDayViewContainer, data: WeekDay) { // Wed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val blue = ResourcesCompat.getColor(resources, R.color.blue, null)
                    val black = ResourcesCompat.getColor(resources, R.color.black, null)
                    container.weekDayTextView.text = data.date.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    )
                    container.dayInMonthTextView.text = "${data.date.dayOfMonth}"
                    if (data.date == selectedDate) {
                        container.dayInMonthTextView.setTextColor(blue)
                        container.weekDayTextView.setTextColor(blue)
                    } else {
                        container.dayInMonthTextView.setTextColor(black)
                        container.weekDayTextView.setTextColor(black)
                    }
                    container.view.setOnClickListener {
                        selectedDate = data.date
                        binding.weekCalendarView.notifyWeekChanged(data)
                        val date = data.date
                        calendar.set(Calendar.YEAR, date.year)
                        calendar.set(Calendar.MONTH, date.month.value - 1)
                        calendar.set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
                        calendar.clearTime()
                        getTasksByDate(calendar.time)

                    }
                }
            }

            override fun create(view: View): WeekDayViewContainer {
                return WeekDayViewContainer(view)
            }

        }
        binding.weekCalendarView.weekScrollListener = { week: Week ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val month =
                    week.days.get(0).date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                binding.monthNameText.text = month
            }
        }
    }

    fun getTasksByDate(date: Date) {
        tasks = TaskDatabase.getInstance(requireContext())
            .getTaskDao().getTasksByDate(date).toMutableList()
        adapter.updateList(tasks)
    }

}
