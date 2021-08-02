package com.erif.alarmmanager.utils.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.erif.alarmmanager.model.ModelItemAlarm

class DatabaseAlarm(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private var db: SQLiteDatabase? = null
    private var values: ContentValues? = null

    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "db_alarm"
        const val TABLE_NAME = "tb_alarm"
        const val KEY_ID = "id"
        const val KEY_TITLE = "title"
        const val KEY_DESC = "desc"
        const val KEY_DURATION = "duration"
        const val KEY_RINGTONE_TITLE = "ringtone_title"
        const val KEY_RINGTONE_URI = "ringtone_uri"
        const val KEY_TIME_LIST = "time_list"
        const val KEY_INDEX = "indexes"
        const val KEY_VIBRATE = "vibrate"
        const val KEY_ACTIVE = "active"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT, "
                + KEY_DESC + " TEXT, "
                + KEY_DURATION + " INTEGER, "
                + KEY_RINGTONE_TITLE + " TEXT, "
                + KEY_RINGTONE_URI + " TEXT, "
                + KEY_TIME_LIST + " TEXT, "
                + KEY_INDEX + " INTEGER, "
                + KEY_VIBRATE + " INTEGER, "
                + KEY_ACTIVE + " INTEGER" + ")")
        db!!.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insert(item: ModelItemAlarm) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TITLE, item.title)
        values.put(KEY_DESC, item.desc)
        values.put(KEY_DURATION, item.duration)
        values.put(KEY_RINGTONE_TITLE, item.ringtoneTitle)
        values.put(KEY_RINGTONE_URI, item.ringtoneUri)
        values.put(KEY_TIME_LIST, item.timeList)
        values.put(KEY_INDEX, item.index)
        values.put(KEY_VIBRATE, item.vibrate)
        values.put(KEY_ACTIVE, item.active)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getValue(): Int {
        val db = this.readableDatabase
        val cnt = DatabaseUtils.queryNumEntries(db, TABLE_NAME)
        db.close()
        return cnt.toInt()
    }

    @SuppressLint("Recycle")
    fun getAlarm(): MutableList<ModelItemAlarm> {
        val list: MutableList<ModelItemAlarm> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $KEY_DURATION ASC"
        val cursor: Cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val item = ModelItemAlarm()
                item.id = cursor.getInt(0)
                item.title = cursor.getString(1)
                item.desc = cursor.getString(2)
                item.duration = cursor.getInt(3)
                item.ringtoneTitle = cursor.getString(4)
                item.ringtoneUri = cursor.getString(5)
                item.timeList = cursor.getString(6)
                item.index = cursor.getInt(7)
                item.vibrate = cursor.getInt(8)
                item.active = cursor.getInt(9)
                list.add(item)
            } while (cursor.moveToNext())
        }
        return list
    }

    @SuppressLint("Recycle")
    fun getAlarmActive(): MutableList<ModelItemAlarm> {
        val list: MutableList<ModelItemAlarm> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $KEY_ACTIVE='1'"
        val cursor: Cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val item = ModelItemAlarm()
                item.id = cursor.getInt(0)
                item.title = cursor.getString(1)
                item.desc = cursor.getString(2)
                item.duration = cursor.getInt(3)
                item.ringtoneTitle = cursor.getString(4)
                item.ringtoneUri = cursor.getString(5)
                item.timeList = cursor.getString(6)
                item.index = cursor.getInt(7)
                item.vibrate = cursor.getInt(8)
                item.active = cursor.getInt(9)
                list.add(item)
            } while (cursor.moveToNext())
        }
        return list
    }

    @SuppressLint("Recycle")
    fun getAlarm(id: Int): ModelItemAlarm {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $KEY_ID='$id'"
        val cursor: Cursor = db.rawQuery(query, null)
        val item = ModelItemAlarm()
        if (cursor.moveToFirst()) {
            do {
                item.id = cursor.getInt(0)
                item.title = cursor.getString(1)
                item.desc = cursor.getString(2)
                item.duration = cursor.getInt(3)
                item.ringtoneTitle = cursor.getString(4)
                item.ringtoneUri = cursor.getString(5)
                item.timeList = cursor.getString(6)
                item.index = cursor.getInt(7)
                item.vibrate = cursor.getInt(8)
                item.active = cursor.getInt(9)
            } while (cursor.moveToNext())
        }
        return item
    }

    @SuppressLint("Recycle")
    fun getLastID(): Int {
        var id = 0
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $KEY_ID DESC LIMIT 1"
        val cursor: Cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(0)
            } while (cursor.moveToNext())
        }
        return id
    }

    // Init Update
    private fun update(){
        db = this.writableDatabase
        values = ContentValues()
    }

    // Update Count
    fun updateTimeList(id: Int, timeList: String) {
        update()
        values?.put(KEY_TIME_LIST, timeList)
        db?.update(TABLE_NAME, values, "$KEY_ID='$id'", null)
        db?.close()
    }

    // Update Count
    fun updateIndex(id: Int, index: Int) {
        update()
        values?.put(KEY_INDEX, index)
        db?.update(TABLE_NAME, values, "$KEY_ID='$id'", null)
        db?.close()
    }

    // Update Active
    fun update(id: Int, active: Int) {
        update()
        values?.put(KEY_ACTIVE, active)
        db?.update(TABLE_NAME, values, "$KEY_ID='$id'", null)
        db?.close()
    }

    // Update Item
    fun update(id: Int, item: ModelItemAlarm) {
        update()
        values?.put(KEY_TITLE, item.title)
        values?.put(KEY_DESC, item.desc)
        values?.put(KEY_DURATION, item.duration)
        values?.put(KEY_RINGTONE_TITLE, item.ringtoneTitle)
        values?.put(KEY_RINGTONE_URI, item.ringtoneUri)
        values?.put(KEY_VIBRATE, item.vibrate)
        db?.update(TABLE_NAME, values, "$KEY_ID='$id'", null)
        db?.close()
    }

    // Update Active
    fun deleteAlarm(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$KEY_ID=$id", null)
        db.close()
    }

    /*fun deleteAllAlarm() {
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_NAME"
        db.execSQL(query)
    }*/

}