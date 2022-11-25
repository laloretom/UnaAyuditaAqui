package com.unaayuditaaqui.unaayuditaaqui

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Service (
    var uidAuthor: String? = null,
    var nameAuthor: String? = null,
    var serviceTitle: String? = null,
    var type: String? = null,
    var description: String? = null,
    var category: String? = null,
    val date:   String? = null,
    val dateInverted: String? = null,
    var url: String? = null,
    var idService: String? = null,
    @Exclude val key: String? = null
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uidAuthor" to uidAuthor,
            "nameAuthor" to nameAuthor,
            "serviceTitle" to serviceTitle,
            "type" to type,
            "description" to description,
            "category" to category,
            "date" to date,
            "dateInverted" to dateInverted,
            "url" to url,
            "idService" to idService
        )}}
