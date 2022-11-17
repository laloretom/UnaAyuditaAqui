package com.unaayuditaaqui.unaayuditaaqui

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Request (
    var toUid: String? = null,
    var toName: String? = null,
    var fromUid: String? = null,
    var fromName: String? = null,
    var serviceTitle: String? = null,
    var idService: String? = null,
    var message: String? = null,
    var state: String? = null,
    val idRequest: String? = null,
    @Exclude val key: String? = null
) {
    
    @Exclude
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "toUid" to toUid,
            "toName" to toName,
            "fromUid" to fromUid,
            "fromName" to fromName,
            "serviceTitle" to serviceTitle,
            "idService" to idService,
            "message" to message,
            "state" to state,
            "idRequest" to idRequest
        )}}