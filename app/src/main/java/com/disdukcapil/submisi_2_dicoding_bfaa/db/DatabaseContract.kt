package com.disdukcapil.submisi_2_dicoding_bfaa.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class UserColumns : BaseColumns{
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR_URL = "avatar_url"
        }
    }

}