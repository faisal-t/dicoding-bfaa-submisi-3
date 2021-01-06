package com.disdukcapil.submisi_2_dicoding_bfaa

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataModel(
    var username: String? = "",
    var repository: String? = "",
    var company: String? = "",
    var name: String? = "",
    var location: String? = "",
    var following: String? = "",
    var folowers: String? = "",
    var avatar: String? = ""
) : Parcelable
