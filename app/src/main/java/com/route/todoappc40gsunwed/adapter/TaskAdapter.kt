package com.route.todoappc40gsunwed.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.route.todoappc40gsunwed.R
import com.route.todoappc40gsunwed.callbacks.SetOnItemClickListner
import com.route.todoappc40gsunwed.database.model.Task
import com.route.todoappc40gsunwed.databinding.ItemTaskBinding

class TaskAdapter(var tasksList: List<Task>) : Adapter<TaskAdapter.TaskViewHolder>() {
    var onDeleteListner: SetOnItemClickListner? = null
    var onEditListner: SetOnItemClickListner? = null
    var onButtonClick: SetOnItemClickListner? = null
    var onDoneBtnClickListner: SetOnItemClickListner? = null
    class TaskViewHolder(val binding: ItemTaskBinding) : ViewHolder(binding.root) {
        fun changeTaskStatus(isDone: Boolean){
            if(isDone){
                binding.draggingBar.setImageResource(R.drawable.dragging_bar_done)
                binding.title.setTextColor(Color.GREEN)
                binding.btnTaskIsDone.setImageResource(R.drawable.done)
            }
            else {
                binding.draggingBar.setImageResource(R.drawable.dragging_bar)
                binding.title.setTextColor(Color.BLUE)
                binding.btnTaskIsDone.setImageResource(R.drawable.check_mark)
            }
        }
        fun bind(task: Task) {
            binding.time.text = task.time
            binding.title.text = task.title
            changeTaskStatus(task.isDone!!)
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
        holder.binding.leftView.setOnClickListener{
            onDeleteListner?.onItemClick(position, item)
        }
        holder.binding.title.setOnClickListener{
            onEditListner?.onItemClick(position, item)
        }
        holder.binding.dragItem.setOnClickListener{
            onButtonClick?.onItemClick(position,item)
        }
        holder.binding.btnTaskIsDone.setOnClickListener{
            onDoneBtnClickListner?.onItemClick(position, item)
        }
    }

    fun updateList(tasks: List<Task>) {
        tasksList = tasks
        notifyDataSetChanged()
    }
}