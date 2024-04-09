package com.example.crud.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.crud.model.TaskListModel

class DatabaseHelper(context: Context) :  SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private val DB_NAME = "task"
        private val DB_VERSION = 1
        private val TABLE_NAME = "TaskList"
        private val ID = "taskListId"
        private val TASK_NAME = "taskName"
        private val TASK_DETAILS = "taskDetails"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $TASK_NAME TEXT, $TASK_DETAILS TEXT);"
        p0?.execSQL(CREATE_TABLE);
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        p0?.execSQL(DROP_TABLE)
        onCreate(p0)
    }

    fun getAllTask(): List<TaskListModel> {
        val taskList = ArrayList<TaskListModel>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val tasks = TaskListModel()
                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    tasks.name = cursor.getString(cursor.getColumnIndex(TASK_NAME))
                    tasks.details = cursor.getString(cursor.getColumnIndex(TASK_DETAILS))
                    taskList.add(tasks)
                } while(cursor.moveToNext())
            }
        }
        cursor.close()
        return taskList
    }

    fun addTask(tasks: TaskListModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME, tasks.name)
        values.put(TASK_DETAILS, tasks.details)

        val _success = db.insert(TABLE_NAME, null, values)
        db.close()

        return (Integer.parseInt("$_success") != -1)
    }

    fun getTask(_id: Int): TaskListModel {
        val tasks = TaskListModel()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.use { // Utiliza el bloque use para cerrar el cursor automáticamente después de su uso.
            if (it.moveToFirst()) {
                tasks.id = it.getInt(it.getColumnIndex(ID))
                tasks.name = it.getString(it.getColumnIndex(TASK_NAME))
                tasks.details = it.getString(it.getColumnIndex(TASK_DETAILS))
            }
        }

        return tasks
    }

    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        val rowsAffected = db.delete(TABLE_NAME, "$ID = ?", arrayOf(_id.toString()))
        db.close()

        return rowsAffected > 0
    }

    fun updateTask(tasks: TaskListModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME, tasks.name)
        values.put(TASK_DETAILS, tasks.details)
        val rowsAffected = db.update(TABLE_NAME, values, "$ID = ?", arrayOf(tasks.id.toString()))
        db.close()

        return rowsAffected > 0
    }
}