package com.disdukcapil.submisi_2_dicoding_bfaa.entity

import android.os.Parcelable
import com.disdukcapil.submisi_2_dicoding_bfaa.DataModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id : Int = 0,
    var username : String ?= null,
    var avatar_url : String ?= null
) : Parcelable {
    fun toDataModel() : DataModel = DataModel(
            username = username
    )
}
