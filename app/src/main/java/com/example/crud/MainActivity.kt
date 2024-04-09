package com.example.crud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.crud.adapter.TaskListAdapter
import com.example.crud.database.DatabaseHelper
import com.example.crud.model.TaskListModel

class MainActivity : AppCompatActivity() {
    lateinit var recycler_task: RecyclerView
    lateinit var btn_add: Button
    var taskListAdapter: TaskListAdapter ?= null
    var dbHandler: DatabaseHelper ?= null
    var taskList: List<TaskListModel> = ArrayList<TaskListModel>()
    var linearLayoutManager: LinearLayoutManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_task = findViewById(R.id.rvList)
        btn_add = findViewById(R.id.addItems)

        dbHandler = DatabaseHelper(this)
        fetchList()

        btn_add.setOnClickListener {
            val i = Intent(applicationContext, ActivityAddTask::class.java)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchList()
    }

    private fun fetchList() {
        taskList = dbHandler!!.getAllTask()
        taskListAdapter = TaskListAdapter(taskList, applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        recycler_task.layoutManager = linearLayoutManager
        recycler_task.adapter = taskListAdapter
        taskListAdapter?.notifyDataSetChanged()
    }
}