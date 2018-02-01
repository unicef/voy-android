package br.com.ilhasoft.voy.models

import com.google.gson.annotations.SerializedName

/**
 * Created by geral on 13/12/17.
 */
data class Theme(@SerializedName("id") val id: Int,
                 @SerializedName("project") val project: String,
                 @SerializedName("bounds") val bounds: ArrayList<ArrayList<Double>>,
                 @SerializedName("name") val name: String,
                 @SerializedName("description") val description: String,
                 @SerializedName("tags") val tags: ArrayList<String>,
                 @SerializedName("color") val color: String,
                 @SerializedName("pin") val pin: String,
                 @SerializedName("reports_count") val reportsCount: Int,
                 @SerializedName("created_on") val createdOn: String)