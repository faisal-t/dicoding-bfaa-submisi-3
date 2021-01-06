package com.disdukcapil.submisi_2_dicoding_bfaa.helper

import android.database.Cursor
import com.disdukcapil.submisi_2_dicoding_bfaa.db.DatabaseContract
import com.disdukcapil.submisi_2_dicoding_bfaa.entity.User

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<User> {
        val notesList = ArrayList<User>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val avatar_url = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))
                notesList.add(User(id, username, avatar_url))
            }
        }
        return notesList
    }

    fun mapCursorToObject(notesCursor: Cursor?): User {
        var user = User()
        notesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
            val avatar_url = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL))
            user = User(id, username, avatar_url)
        }
        return user
    }

}