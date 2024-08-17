package com.route.todoappc40gsunwed.callbacks

import com.route.todoappc40gsunwed.database.model.Task

interface SetOnItemClickListner {
    fun onItemClick(position: Int, task: Task)
}