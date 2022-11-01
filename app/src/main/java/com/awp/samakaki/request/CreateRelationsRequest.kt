package com.awp.samakaki.request

import com.google.gson.annotations.SerializedName

class CreateRelationsRequest (

    @SerializedName("name")
    var name: String,

    @SerializedName("relation_name")
    var relation_name: String,

    @SerializedName("family_tree_id")
    var family_tree_id: String

)