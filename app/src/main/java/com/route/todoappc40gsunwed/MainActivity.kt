package com.route.todoappc40gsunwed

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.route.todoappc40gsunwed.callbacks.OnTaskAddedListener
import com.route.todoappc40gsunwed.database.TaskDatabase
import com.route.todoappc40gsunwed.databinding.ActivityMainBinding
import com.route.todoappc40gsunwed.fragments.AddTaskBottomSheetFragment
import com.route.todoappc40gsunwed.fragments.SettingsFragment
import com.route.todoappc40gsunwed.fragments.TodoListFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var todoListFragment: TodoListFragment
    lateinit var settingsFragment: SettingsFragment
    fun pushFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.todoFragmentContainer.id, fragment)
            .commit()
    }

    // 1- Settings Fragment
    // 2- Edit Task Fragment
    // 3- Update Task State
    // 4- Swipe-to-Delete
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        todoListFragment = TodoListFragment()
        settingsFragment = SettingsFragment()
        binding.fabAdd.setOnClickListener {
            val bottomSheetFragment = AddTaskBottomSheetFragment()
            bottomSheetFragment.onTaskAddedListener = object : OnTaskAddedListener {
                override fun onTaskAdded() {
                    if (todoListFragment.isVisible)
                        if (todoListFragment.selectedDate == null)
                            todoListFragment.getTasksFromDatabase()
                        else
                            todoListFragment.getTasksByDate(todoListFragment.calendar.time)
                }

            }
            bottomSheetFragment.show(supportFragmentManager, "add_todo")

        }
        binding.todoBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_list -> pushFragment(todoListFragment)
                R.id.navigation_settings -> pushFragment(settingsFragment)
            }
            return@setOnItemSelectedListener true
        }
        binding.todoBottomNavigationView.selectedItemId = R.id.navigation_list
        // CRUD Operations
//  1- Create
//  2- Read
//  3- Update
//  4- Delete
        // File  -> READ
        // Files Manipulation
        // SQL -> Structured Query Language  (V)
        // Querying
        val query = "SELET * FROM CUSTOMERS"   // 1- Runtime Errors
//                                             // 2- Boilerplate Code
        // Room Database -> SQLite <-> Developer
        //                  Annotation Processors
    }

    // 2 Fragments Communicate with each other :-
    //          1- Callbacks making sure that activity between
    //          2- Shared View Model

    // Runtime Permissions -> Google Maps Integration
    // SOLID Design Principles or Jetpack Compose
}