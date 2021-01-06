package com.disdukcapil.submisi_2_dicoding_bfaa.db

import android.net.Uri
import android.provider.BaseColumns


object DatabaseContract {

    const val AUTHORITY = "com.disdukcapil.submisi_2_dicoding_bfaa"
    const val SCHEME = "content"

    internal class UserColumns : BaseColumns{
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR_URL = "avatar_url"

            // untuk membuat URI content://com.dicoding.picodiploma.mynotesapp/note
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }

}