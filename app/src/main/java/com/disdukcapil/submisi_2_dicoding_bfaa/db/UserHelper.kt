package com.disdukcapil.submisi_2_dicoding_bfaa.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.disdukcapil.submisi_2_dicoding_bfaa.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.disdukcapil.submisi_2_dicoding_bfaa.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.disdukcapil.submisi_2_dicoding_bfaa.db.DatabaseContract.UserColumns.Companion._ID
import kotlin.jvm.Throws

class UserHelper(context: Context) {

    companion object{

        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper

        private lateinit var database: SQLiteDatabase

        private var INSTANCE: UserHelper? = null
        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }
    }

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll() : Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
        Log.d("insert",values.toString())
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$id'", null)
        Log.d("delete",id.toString())
    }

}