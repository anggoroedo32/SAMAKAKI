package com.qatros.samakaki.request

import com.google.gson.annotations.SerializedName

class CreateRelationsRequest (

    @SerializedName("name")
    var name: String,

    @SerializedName("relation_name")
    var relation_name: String

)