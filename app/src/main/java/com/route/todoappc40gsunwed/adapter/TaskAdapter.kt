package com.route.todoappc40gsunwed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.route.todoappc40gsunwed.database.model.Task
import com.route.todoappc40gsunwed.databinding.ItemTaskBinding

class TaskAdapter(var tasksList: List<Task>) : Adapter<TaskAdapter.TaskViewHolder>() {


    class TaskViewHolder(val binding: ItemTaskBinding) : ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskTime.text = task.time
            binding.taskTitle.text = task.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = tasksList.get(position)
        holder.bind(item)
    }

    fun updateList(tasks: List<Task>) {
        tasksList = tasks
        notifyDataSetChanged()
    }
}