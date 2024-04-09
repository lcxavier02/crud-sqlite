package com.example.crud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.crud.database.DatabaseHelper
import com.example.crud.model.TaskListModel

class ActivityAddTask : AppCompatActivity() {
    lateinit var btn_save: Button
    lateinit var btn_del: Button
    lateinit var et_name: EditText
    lateinit var et_details: EditText
    var dbHandler: DatabaseHelper ?= null
    var isEditmode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        btn_save = findViewById(R.id.btnSave)
        btn_del = findViewById(R.id.btnDelete)
        et_name = findViewById(R.id.etName)
        et_details = findViewById(R.id.etDetails)

        dbHandler = DatabaseHelper(this)

        if (intent != null && intent.getStringExtra("Mode") == "E") {
            isEditmode = true
            btn_save.text = "Update data"
            btn_del.visibility = View.VISIBLE

            val tasks: TaskListModel = dbHandler!!.getTask(intent.getIntExtra("taskListId", 0))
            et_name.setText(tasks.name)
            et_details.setText(tasks.details)
        } else {
            isEditmode = false
            btn_save.text = "Save data"
            btn_del.visibility = View.GONE
        }

        btn_save.setOnClickListener {
            var success: Boolean = false
            val tasks: TaskListModel = TaskListModel()
            if (isEditmode) {
                tasks.id = intent.getIntExtra("taskListId", 0)
                tasks.name = et_name.text.toString()
                tasks.details = et_details.text.toString()

                success = dbHandler!!.updateTask(tasks) as Boolean
            } else {
                tasks.name = et_name.text.toString()
                tasks.details = et_details.text.toString()

                success = dbHandler!!.addTask(tasks) as Boolean
            }

            if (success) {
                val i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
                finish()
            } else {
                Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_LONG).show()
            }
        }

        btn_del.setOnClickListener {
            val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Clik yes if you want to delete the task")
                .setPositiveButton("Yes", {dialog, i ->
                    val success = dbHandler!!.deleteTask(intent.getIntExtra("taskListId", 0)) as Boolean
                    if (success)
                        finish()
                    dialog.dismiss()
                })
                .setNegativeButton("No", {dialog, i ->
                    dialog.dismiss()
                })
            dialog.show()
        }
    }
}