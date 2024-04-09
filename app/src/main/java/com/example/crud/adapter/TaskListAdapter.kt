package com.example.crud.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.crud.ActivityAddTask
import com.example.crud.R
import com.example.crud.model.TaskListModel

class TaskListAdapter(taskList: List<TaskListModel>, internal var context: Context)
    : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>()
{
    internal var taskList: List<TaskListModel> = ArrayList()

    init {
        this.taskList = taskList
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.txtName)
        var details: TextView = view.findViewById(R.id.txtDetails)
        var btn_edit: Button = view.findViewById(R.id.editBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_task_list, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tasks = taskList[position]
        holder.name.text = tasks.name
        holder.details.text = tasks.details

        holder.btn_edit.setOnClickListener {
            val i = Intent(context, ActivityAddTask::class.java)
            i.putExtra("Mode", "E")
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.putExtra("taskListId", tasks.id)
            context.startActivity(i)
        }
    }
}